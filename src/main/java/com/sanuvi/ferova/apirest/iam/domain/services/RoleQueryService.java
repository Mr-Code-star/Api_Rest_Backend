package com.sanuvi.ferova.apirest.iam.domain.services;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetAllRolesQuery;

import java.util.List;

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

}