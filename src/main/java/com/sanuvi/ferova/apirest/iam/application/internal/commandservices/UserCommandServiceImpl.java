package com.sanuvi.ferova.apirest.iam.application.internal.commandservices;

import com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.hashing.HashingService;
import com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.tokens.TokenService;
import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.*;
import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.*;
import com.sanuvi.ferova.apirest.iam.domain.services.UserCommandService;
import com.sanuvi.ferova.apirest.iam.infrastructure.email.resend.services.EmailServiceImpl;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.RoleRepository;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.UserRepository;
import com.sanuvi.ferova.apirest.shared.domain.exceptions.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final EmailServiceImpl emailService;
    private final RoleRepository roleRepository;


    @Override
    public Optional<User> handle(CreateStaffUserCommand command) {

        if (!hasValidStaffRole(command.roles())) {
            log.warn("El usuario debe tener rol NURSE o ADMIN");
            return Optional.empty();
        }

        if (!validateUniqueness(command.dni(), command.email(), command.phone())) {
            return Optional.empty();
        }

        Role roleToAssign = getStaffRole(command.roles());
        Role existingRole = roleRepository.findByName(roleToAssign.getName())
                .orElseThrow(() -> new RuntimeException("Role name not found"));

        var user = new User(
                command.name(),
                command.lastName(),
                new Password(hashingService.encode(command.password())),
                existingRole,
                new Dni(command.dni()),
                new Email(command.email()),
                new Phone(command.phone())
        );

        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }

    @Override
    public Optional<User> handle(RegisterMotherCommand command) {

        if(!validateUniqueness(command.dni(), command.email(),command.phone())) {
            return Optional.empty();
        }

        Optional<Role> motherRole = roleRepository.findByName(Roles.MOTHER);

        if(motherRole.isEmpty()) {
            log.error("Rol MOTHER no encontrado en la base de datos");
            return Optional.empty();
        }

        var user = new User(
                command.name(),
                command.lastName(),
                new Password(hashingService.encode(command.password())),
                motherRole.get(),
                new Dni(command.dni()),
                new Email(command.email()),
                new Phone(command.phone())
                );

        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(LoginUserCommand command) {
        var user = userRepository.findByDni(new Dni(command.dni()));
        if (user.isEmpty())
            throw new InvalidCredentialsException("User not found");
        if (!hashingService.matches(command.password(), user.get().getPassword().value())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        // Generamos el Token
        var token = tokenService.generateToken(user.get().getDni().value());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Optional<Void> handle(RequestResetCodeCommand command) {
        return Optional.empty();
    }

    @Override
    public Optional<Void> handle(VerifyResetCodeCommand command) {
        return Optional.empty();
    }

    @Override
    public Optional<Void> handle(ResetPasswordCommand command) {
        return Optional.empty();
    }

    // Metodos Auxiliares

    /**
     * Valida que el DNI, Email y Teléfono sean únicos en la base de datos
     *
     * @param dni el DNI a validar
     * @param email el Email a validar
     * @param phone el Teléfono a validar
     * @return true si son únicos, false si ya existe alguno
     */
    private boolean validateUniqueness(String dni, String email, String phone) {
        try {
            Dni dniVO = new Dni(dni);
            Email emailVO = new Email(email);
            Phone phoneVO = new Phone(phone);

            if (userRepository.existsByDni(dniVO)) {
                log.warn("DNI ya registrado: {}", dni);
                return false;
            }

            if (userRepository.existsByEmail(emailVO)) {
                log.warn("Email ya registrado: {}", email);
                return false;
            }

            if (userRepository.existsByPhone(phoneVO)) {
                log.warn("Teléfono ya registrado: {}", phone);
                return false;
            }

            return true;

        } catch (IllegalArgumentException e) {
            log.error("Error de validación al verificar unicidad: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Error al validar unicidad: {}", e.getMessage());
            return false;
        }
    }

    // Métodos auxiliares para mantener el código limpio
    private boolean hasValidStaffRole(List<Role> roles) {
        return roles.stream()
                .anyMatch(role -> role.getName() == Roles.NURSE || role.getName() == Roles.ADMIN);
    }

    private Role getStaffRole(List<Role> roles) {
        return roles.stream()
                .filter(role -> role.getName() == Roles.NURSE || role.getName() == Roles.ADMIN)
                .findFirst()
                .orElse(roles.get(0));
    }
}
