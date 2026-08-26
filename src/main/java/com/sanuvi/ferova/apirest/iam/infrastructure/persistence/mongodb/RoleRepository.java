package com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, Long> {

    /**
     * Busca un rol por su nombre (enum)
     *
     * @param name el nombre del rol
     * @return Optional con el rol encontrado
     */
    Optional<Role> findByName(Roles name);

    /**
     * Verifica si existe un rol con el nombre especificado
     *
     * @param name el nombre del rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(Roles name);

    /**
     * Busca roles activos
     *
     * @return lista de roles activos
     */
    List<Role> findByIsActiveTrue();

    /**
     * Busca un rol por nombre ignorando mayúsculas/minúsculas
     *
     * @param name el nombre del rol
     * @return Optional con el rol encontrado
     */
    Optional<Role> findByNameIgnoreCase(String name);

    /**
     * Buscar role por id
     */
    Optional<Role> findById(Long id);
}