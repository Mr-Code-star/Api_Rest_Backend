package com.sanuvi.ferova.apirest.iam.interfaces.acl;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.CreateStaffUserCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.RegisterMotherCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetUserByDniQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetUserByEmailQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetUserByIdQuery;
import com.sanuvi.ferova.apirest.iam.domain.services.UserCommandService;
import com.sanuvi.ferova.apirest.iam.domain.services.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Fachada para el contexto de IAM (Identity and Access Management)
 *     Esta clase proporciona una interfaz simplificada para que otros
 *     bounded contexts (módulos) puedan interactuar con el contexto de IAM.
 *
 *         Simplifica la interacción con el subsistema IAM</li>
 *         Aísla la complejidad interna del módulo IAM</li>
 *         Reduce el acoplamiento entre módulos</li>
 *         Centraliza la lógica de acceso a IAM</li>
 *         Facilita el mantenimiento y evolución del sistema</li>
 *
 * @author
 * @version 1.0
 * @see UserCommandService
 * @see UserQueryService
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class IamContextFacade {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;


    /**
     * Crea un usuario staff (Administrador o Enfermera)
     *
     * @param name nombre del usuario
     * @param lastName apellido del usuario
     * @param dni DNI del usuario
     * @param email email del usuario
     * @param phone teléfono del usuario
     * @param password contraseña del usuario
     * @param roleId role id del usuario
     * @return el ID del usuario creado, o null si falla
     */
    public String  createStaffUser(String name, String lastName, String dni,
                                  String email, String phone, String password,
                                  String roleId) {

        var createStaffuserCommand = new CreateStaffUserCommand(name, lastName, dni, email, phone, password, roleId);
        var results = userCommandService.handle(createStaffuserCommand);
        if (results.isEmpty()) {
            log.warn("Facade: No se pudo crear el usuario staff con email: {}", email);
            return null;
        }
        return results.get().getId();
    }

    /**
     * Registra una nueva madre
     *
     * @param name nombre de la madre
     * @param lastName apellido de la madre
     * @param dni DNI de la madre
     * @param email email de la madre
     * @param phone teléfono de la madre
     * @param password contraseña de la madre
     * @return el ID de la madre creada, o null si falla
     */
    public String  registerMother(String name, String lastName, String dni,
                                 String email, String phone, String password) {

        var registerMotherCommand = new RegisterMotherCommand(name, lastName, dni, email, phone, password);
        var results = userCommandService.handle(registerMotherCommand);
        if (results.isEmpty()) {
            log.warn("Facade: No se pudo registrar la madre con email: {}", email);
            return null;
        }
        return results.get().getId();
    }

    // ===== MÉTODOS DE CONSULTA DE USUARIOS =====

    /**
     * Obtiene el ID de un usuario por su email
     *
     * @param email el email del usuario
     * @return el ID del usuario, o null si no existe
     */
    public String getUserIdByEmail(String email) {

            var query = new GetUserByEmailQuery(email);
            var result = userQueryService.handle(query);
            if (result.isEmpty()) {
                return null;
            }
            return result.get().getId();
    }

    /**
     * Obtiene el ID de un usuario por su DNI
     *
     * @param dni el DNI del usuario
     * @return el ID del usuario, o null si no existe
     */
    public String getUserIdByDni(String dni) {
        var query = new GetUserByDniQuery(dni);
        var result = userQueryService.handle(query);
        if (result.isEmpty()) {
            return null;
        }
        return result.get().getId();
    }

    /**
     * Obtiene un usuario por su ID
     *
     * @param userId el ID del usuario
     * @return el usuario, o null si no existe
     */
    public User getUserById(Long userId) {
        log.info("Facade: Obteniendo usuario por ID: {}", userId);

        try {
            var query = new GetUserByIdQuery(userId);
            Optional<User> result = userQueryService.handle(query);

            if (result.isEmpty()) {
                return null;
            }

            return result.get();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si existe un usuario con el email especificado
     *
     * @param email el email a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsUserByEmail(String email) {

        try {
            var userId = getUserIdByEmail(email);
            boolean exists = userId != null;
            return exists;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si existe un usuario con el DNI especificado
     *
     * @param dni el DNI a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsUserByDni(String dni) {

        try {
            var userId = getUserIdByDni(dni);
            boolean exists = userId != null;
            return exists;

        } catch (Exception e) {
            return false;
        }
    }

    // ===== MÉTODOS DE VALIDACIÓN =====

    /**
     * Valida las credenciales de un usuario
     *
     * @param dni el DNI del usuario
     * @param password la contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean validateCredentials(String dni, String password) {
        try {
            var query = new GetUserByDniQuery(dni);
            var result = userQueryService.handle(query);

            if (result.isEmpty()) {
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}