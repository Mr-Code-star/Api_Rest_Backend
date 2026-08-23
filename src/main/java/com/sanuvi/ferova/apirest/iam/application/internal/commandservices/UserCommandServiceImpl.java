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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Transactional
    public Optional<User> handle(CreateStaffUserCommand command) {

        String roleName = command.roleName();

        if(!"NURSE".equals(roleName) && !"ADMIN".equals(roleName))  {
            log.warn("Rol de staff inválido: {}", roleName);
            return Optional.empty();
        }

        if(!validateUniqueness(command.dni(), command.email(), command.phone()))
            return Optional.empty();

        // Buscar el rol por nombre
        var rolesEnum = Roles.valueOf(roleName);
        var roleOpt = roleRepository.findByName(rolesEnum);

        if (roleOpt.isEmpty()) {
            log.error("Rol no encontrado: {}", roleName);
            return Optional.empty();
        }

        var user = new User(
                command.name(),
                command.lastName(),
                new Password(hashingService.encode(command.password())),
                roleOpt.get(),
                new Dni(command.dni()),
                new Email(command.email()),
                new Phone(command.phone())
        );

        User savedUser = userRepository.save(user);

        return Optional.of(savedUser);
    }

    @Override
    @Transactional
    public Optional<User> handle(RegisterMotherCommand command) {

        if(!validateUniqueness(command.dni(), command.email(),command.phone())) {
            return Optional.empty();
        }

        var motherRole = roleRepository.findByName(Roles.MOTHER);

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
    @Transactional(readOnly = true)
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
    @Transactional
    public Optional<Void> handle(RequestResetCodeCommand command) {
        var email = new Email(command.email());

        var userOpt = userRepository.findByEmail(email);

        if(userOpt.isEmpty()) {
          log.warn("Usuario no encontrado con email: {}", command.email());
          return Optional.empty();
        }

        User user = userOpt.get();

        // Generar codigo de 4 digitos (1000-9999)
        String code = String.valueOf((int) (Math.random() * 9000) + 1000);
        // Expira en 10 minutos
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);

        // Guardar codigo en el usuario
        user.setResetCode(code);
        user.setResetCodeExpiry(expiresAt);
        userRepository.save(user);

        // Enviar email con el codigo
        emailService.sendResetCode(command.email(), code);

        return Optional.of(null);
    }

    @Override
    @Transactional
    public Optional<Void> handle(VerifyResetCodeCommand command) {
        var email = new Email(command.email());
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.warn("Usuario no encontrado con email: {}", command.email());
            return Optional.empty();
        }

        User user = userOpt.get();

        if(!user.isResetCodeValid(command.code())){
            log.warn("Código inválido o expirado para email: {}", command.email());
            return Optional.empty();
        }

        log.info("Código verificado exitosamente para: {}", command.email());
        return Optional.of(null);

    }

    @Override
    @Transactional
    public Optional<Void> handle(ResetPasswordCommand command) {
        var verifyCommand = new VerifyResetCodeCommand(
                command.email(),
                command.code()
        );

        Optional<Void> verifyResult = handle(verifyCommand);

        if (verifyResult.isEmpty()) {
            log.warn("Código inválido para resetear contraseña: {}", command.email());
            return Optional.empty();
        }

        var email = new Email(command.email());
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            log.warn("Usuario no encontrado con email: {}", command.email());
            return Optional.empty();
        }

        User user = userOpt.get();

        // Hashear la nueva contraseña
        String hashedPassword = hashingService.encode(command.newPassword());
        Password newPassword = new Password(hashedPassword);

        // Actualizar la contraseña
        user.changePassword(newPassword);
        user.clearResetCode();
        userRepository.save(user);

        log.info("Contraseña restablecida exitosamente para: {}", command.email());
        return Optional.of(null);


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
