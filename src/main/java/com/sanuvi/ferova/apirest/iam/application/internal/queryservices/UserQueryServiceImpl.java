package com.sanuvi.ferova.apirest.iam.application.internal.queryservices;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.*;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Dni;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Email;
import com.sanuvi.ferova.apirest.iam.domain.services.UserQueryService;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> handle(GetAllStaffUsersQuery query) {

        List<User> staffUser = userRepository.findAll().stream()
                .filter(user -> {
                    String roleName = user.getRoleName();
                    return "ADMIN".equals(roleName) || "NURSE".equals(roleName);
                }).toList();

        return staffUser;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> handle(GetMothersQuery query) {

        List<User> mothers = userRepository.findAll().stream()
                .filter(user -> "MOTHER".equals(user.getRoleName()))
                .toList();

        return mothers;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> handle(GetUserByEmailQuery query) {
        var email = new Email(query.email());
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> handle(GetUserByDniQuery query) {
        var dni = new Dni(query.Dni());
        return userRepository.findByDni(dni);
    }

    @Override
    public List<User> handle(GetMothersBySearchTermQuery query) {
        String searchTerm = query.searchTerm().toLowerCase().trim();

        if (searchTerm.isEmpty()){
            log.warn("Término de búsqueda vacío");
            return List.of();
        }

        List<User> allUsers = userRepository.findAll();

        // Busqueda de madres via DNI, NOMBRE Y APELLIDO
        List<User> mothers = allUsers.stream()
                .filter(user -> "MOTHER".equals(user.getRoleName()))
                .filter(user -> matchesSearchTerm(user, searchTerm))
                .toList();


        return mothers;
    }

    // ===== MÉTODOS AUXILIARES =====

    /**
     * Verifica si un usuario coincide con el término de búsqueda
     *
     * @param user el usuario a verificar
     * @param searchTerm el término de búsqueda en minúsculas
     * @return true si el usuario coincide con el término
     */
    private boolean matchesSearchTerm(User user, String searchTerm) {
        // Buscar por nombre
        String name = user.getName().toLowerCase();
        if (name.contains(searchTerm)) {
            return true;
        }

        // Buscar por apellido
        String lastName = user.getLastName().toLowerCase();
        if (lastName.contains(searchTerm)) {
            return true;
        }

        // Buscar por nombre completo (name + lastName)
        String fullName = (user.getName() + " " + user.getLastName()).toLowerCase();
        if (fullName.contains(searchTerm)) {
            return true;
        }

        // Buscar por nombre completo invertido (lastName + name)
        String fullNameReversed = (user.getLastName() + " " + user.getName()).toLowerCase();
        if (fullNameReversed.contains(searchTerm)) {
            return true;
        }

        // Buscar por DNI
        String dni = user.getDni().value();
        if (dni.contains(searchTerm)) {
            return true;
        }

        return false;
    }
}
