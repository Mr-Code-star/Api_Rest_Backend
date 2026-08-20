package com.sanuvi.ferova.apirest.iam.domain.services;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;


/**
 * Servicio de comandos de usuario
 * <p>
 *     Esta interfaz representa el servicio para manejar los comandos de usuario.
 * </p>
 */
public interface UserCommandService {

    /**
     * Maneja el comando para crear un usuario staff (Administrador o Enfermero)
     *
     * @param command el {@link CreateStaffUserCommand} con los datos del usuario staff
     * @return un {@link Optional} que contiene el {@link User} creado, o vacío si falla
     */
    Optional<User> handle(CreateStaffUserCommand command);

    /**
     * Maneja el comando para registrar una madre
     *
     * @param command el {@link RegisterMotherCommand} con los datos de la madre
     * @return un {@link Optional} que contiene el {@link User} creado, o vacío si falla
     */
    Optional<User> handle(RegisterMotherCommand command);

    /**
     * Maneja el comando para iniciar sesión de usuario
     *
     * @param command el {@link LoginUserCommand} con las credenciales de inicio de sesión
     * @return un {@link Optional} que contiene un {@link ImmutablePair} con el {@link User}
     *         y el token JWT, o vacío si falla la autenticación
     */
    Optional<ImmutablePair<User, String>> handle(LoginUserCommand command);

    /**
     * Maneja el comando para solicitar un código de restablecimiento de contraseña
     *
     * @param command el {@link RequestResetCodeCommand} con el email del usuario
     * @return un {@link Optional} que indica éxito (conteniendo null) o fracaso (vacío)
     */
    Optional<Void> handle(RequestResetCodeCommand command);

    /**
     * Maneja el comando para verificar un código de restablecimiento de contraseña
     *
     * @param command el {@link VerifyResetCodeCommand} con el email y el código
     * @return un {@link Optional} que indica éxito (conteniendo null) o fracaso (vacío)
     */
    Optional<Void> handle(VerifyResetCodeCommand command);

    /**
     * Maneja el comando para restablecer la contraseña
     *
     * @param command el {@link ResetPasswordCommand} con el email, código y nueva contraseña
     * @return un {@link Optional} que indica éxito (conteniendo null) o fracaso (vacío)
     */
    Optional<Void> handle(ResetPasswordCommand command);
}