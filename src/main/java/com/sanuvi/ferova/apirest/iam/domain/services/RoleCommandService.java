package com.sanuvi.ferova.apirest.iam.domain.services;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.CreateRoleCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.UpdateRoleCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.DeleteRoleCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.SeedRolesCommand;

import java.util.Optional;

/**
 * Servicio de comandos para roles
 * <p>
 *     Esta interfaz define las operaciones de escritura para la gestión de roles.
 * </p>
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public interface RoleCommandService {

    /**
     * Crea un nuevo rol
     *
     * @param command el comando {@link CreateRoleCommand} con los datos del rol
     * @return un {@link Optional} que contiene el rol creado, o vacío si falla
     */
    Optional<Role> handle(CreateRoleCommand command);

    /**
     * Actualiza un rol existente
     *
     * @param command el comando {@link UpdateRoleCommand} con los datos actualizados
     * @return un {@link Optional} que contiene el rol actualizado, o vacío si falla
     */
    Optional<Role> handle(UpdateRoleCommand command);

    /**
     * Elimina (desactiva) un rol
     *
     * @param command el comando {@link DeleteRoleCommand} con el ID del rol
     * @return un {@link Optional} que indica éxito (conteniendo null) o fracaso (vacío)
     */
    Optional<Void> handle(DeleteRoleCommand command);

    /**
     * Sembra los roles por defecto en la base de datos
     *
     * @param command el comando {@link SeedRolesCommand}
     * @return un {@link Optional} que indica éxito o fracaso
     */
    Optional<Void> handle(SeedRolesCommand command);
}