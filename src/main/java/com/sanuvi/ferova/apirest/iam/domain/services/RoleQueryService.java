package com.sanuvi.ferova.apirest.iam.domain.services;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetAllRolesQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetRoleByIdQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetRoleByNameQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetActiveRolesQuery;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de consultas para roles
 * <p>
 *     Esta interfaz define las operaciones de lectura para la gestión de roles.
 * </p>
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public interface RoleQueryService {

    /**
     * Obtiene todos los roles
     *
     * @param query la consulta {@link GetAllRolesQuery}
     * @return una lista con todos los roles
     */
    List<Role> handle(GetAllRolesQuery query);

    /**
     * Obtiene un rol por su ID
     *
     * @param query la consulta {@link GetRoleByIdQuery}
     * @return un {@link Optional} que contiene el rol, o vacío si no existe
     */
    Optional<Role> handle(GetRoleByIdQuery query);

    /**
     * Obtiene un rol por su nombre
     *
     * @param query la consulta {@link GetRoleByNameQuery}
     * @return un {@link Optional} que contiene el rol, o vacío si no existe
     */
    Optional<Role> handle(GetRoleByNameQuery query);

    /**
     * Obtiene todos los roles activos
     *
     * @param query la consulta {@link GetActiveRolesQuery}
     * @return una lista con los roles activos
     */
    List<Role> handle(GetActiveRolesQuery query);
}