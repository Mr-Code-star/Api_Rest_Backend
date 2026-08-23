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
     * Sembra los roles por defecto en la base de datos
     *
     * @param command el comando {@link SeedRolesCommand}
     * @return un {@link Optional} que indica éxito o fracaso
     */
    void handle(SeedRolesCommand command);
}