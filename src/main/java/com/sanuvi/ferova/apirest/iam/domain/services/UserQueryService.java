package com.sanuvi.ferova.apirest.iam.domain.services;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de consultas de usuario
 * <p>
 *     Esta interfaz representa el servicio para manejar las consultas de usuario.
 * </p>
 */
public interface UserQueryService {

    /**
     * Maneja la consulta para obtener todos los usuarios staff (Administradores y Enfermeros)
     *
     * @param query el {@link GetAllStaffUsersQuery} con los parámetros de la consulta
     * @return una {@link List} de {@link User} con los usuarios staff encontrados
     */
    List<User> handle(GetAllStaffUsersQuery query);

    /**
     * Maneja la consulta para obtener todas las madres
     *
     * @param query el {@link GetMothersQuery} con los parámetros de la consulta
     * @return una {@link List} de {@link User} con las madres encontradas
     */
    List<User> handle(GetMothersQuery query);

    /**
     * Maneja la consulta para obtener un usuario por su ID
     *
     * @param query el {@link GetUserByIdQuery} con el ID del usuario
     * @return un {@link Optional} que contiene el {@link User} encontrado, o vacío si no existe
     */
    Optional<User> handle(GetUserByIdQuery query);

    /**
     * Maneja la consulta para obtener un usuario por su email
     *
     * @param query el {@link GetUserByEmailQuery} con el email del usuario
     * @return un {@link Optional} que contiene el {@link User} encontrado, o vacío si no existe
     */
    Optional<User> handle(GetUserByEmailQuery query);

    /**
     * Maneja la consulta para obtener el perfil de un usuario
     *
     * @param query el {@link GetUserByDniQuery} con el ID del usuario
     * @return un {@link Optional} que contiene el {@link User} encontrado, o vacío si no existe
     */
    Optional<User> handle(GetUserByDniQuery query);

    /**
     * Busca madres por término de búsqueda
     *
     * @param query la consulta {@link GetMothersBySearchTermQuery}
     * @return una lista con las madres que coinciden con el término
     */
    List<User> handle(GetMothersBySearchTermQuery query);
}