package com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de usuarios para MongoDB
 *
 *     Esta interfaz es responsable de proporcionar las operaciones relacionadas
 *     con la entidad User en la base de datos MongoDB.
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, Long> {

    // ===== MÉTODOS BÁSICOS =====

    /**
     * Busca un usuario por su DNI
     *
     * @param dni el DNI del usuario
     * @return un {@link Optional} que contiene el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByDni(Dni dni);

    /**
     * Busca un usuario por su Email
     *
     * @param email el Email del usuario
     * @return un {@link Optional} que contiene el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByEmail(Email email);

    /**
     * Busca un usuario por su Teléfono
     *
     * @param phone el Teléfono del usuario
     * @return un {@link Optional} que contiene el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByPhone(Phone phone);

    /**
     * Verifica si existe un usuario con el DNI especificado
     *
     * @param dni el DNI a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByDni(Dni dni);

    /**
     * Verifica si existe un usuario con el Email especificado
     *
     * @param email el Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmail(Email email);

    /**
     * Verifica si existe un usuario con el Teléfono especificado
     *
     * @param phone el Teléfono a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByPhone(Phone phone);

    // ===== MÉTODOS ESPECÍFICOS PARA ROLES =====

    /**
     * Busca una madre por su DNI
     *
     * @param dni el DNI de la madre
     * @return un {@link Optional} que contiene la madre encontrada, o vacío si no existe
     */
    @Query("{ 'dni': ?0, 'role': 'MOTHER' }")
    Optional<User> findMotherByDni(Dni dni);

    /**
     * Busca una madre por su ID
     *
     * @param id el ID de la madre
     * @return un {@link Optional} que contiene la madre encontrada, o vacío si no existe
     */
    @Query("{ '_id': ?0, 'role': 'MOTHER' }")
    Optional<User> findMotherById(Long id);

    /**
     * Busca un enfermero por su ID
     *
     * @param id el ID del enfermero
     * @return un {@link Optional} que contiene el enfermero encontrado, o vacío si no existe
     */
    @Query("{ '_id': ?0, 'role': 'NURSE' }")
    Optional<User> findNurseById(Long id);

    /**
     * Obtiene todos los enfermeros
     *
     * @return una {@link List} con todos los enfermeros
     */
    @Query("{ 'role': 'NURSE' }")
    List<User> findAllNurses();

    /**
     * Obtiene todos los administradores
     *
     * @return una {@link List} con todos los administradores
     */
    @Query("{ 'role': 'ADMIN' }")
    List<User> findAllAdmins();

    // ===== MÉTODOS DE BÚSQUEDA AVANZADA =====

    /**
     * Busca madres por término de búsqueda (nombre o apellido)
     *
     * @param searchTerm el término de búsqueda
     * @return una {@link List} con las madres que coinciden con el término
     */
    @Query("{ 'role': 'MOTHER', $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'lastName': { $regex: ?0, $options: 'i' } } ] }")
    List<User> findMothersBySearchTerm(String searchTerm);

    /**
     * Busca usuarios por nombre o apellido (insensible a mayúsculas)
     *
     * @param searchTerm el término de búsqueda
     * @return una {@link List} con los usuarios que coinciden con el término
     */
    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'lastName': { $regex: ?0, $options: 'i' } } ] }")
    List<User> findUsersBySearchTerm(String searchTerm);

    // ===== MÉTODOS PARA RESET DE CONTRASEÑA =====

    /**
     * Busca un usuario por su email para operaciones de reset de contraseña
     *
     * @param email el Email del usuario
     * @return un {@link Optional} que contiene el usuario encontrado, o vacío si no existe
     */
    Optional<User> findByEmailForReset(Email email);

    /**
     * Busca usuarios con código de reset válido
     *
     * @param currentTime la hora actual para validar expiración
     * @return una {@link List} con los usuarios que tienen código de reset válido
     */
    @Query("{ 'resetCode': { $exists: true }, 'resetCodeExpiry': { $gt: ?0 } }")
    List<User> findUsersWithValidResetCode(LocalDateTime currentTime);

    // ===== MÉTODOS DE ACTUALIZACIÓN (USANDO QUERIES PERSONALIZADAS) =====

    /**
     * Actualiza el código de reset de un usuario
     *
     * @param email el Email del usuario
     * @param resetCode el nuevo código de reset
     * @param resetCodeExpiry la fecha de expiración del código
     */
    @Query("{ 'email': ?0 }")
    void updateResetCode(Email email, String resetCode, LocalDateTime resetCodeExpiry);

    /**
     * Limpia el código de reset de un usuario
     *
     * @param email el Email del usuario
     */
    @Query("{ 'email': ?0 }")
    void clearResetCode(Email email);

    /**
     * Actualiza la contraseña de un usuario
     *
     * @param email el Email del usuario
     * @param newPassword la nueva contraseña encriptada
     */
    @Query("{ 'email': ?0 }")
    void updatePassword(Email email, String newPassword);

    // ===== MÉTODOS DE CONTEO =====

    /**
     * Cuenta el número de usuarios por rol
     *
     * @param role el rol a contar
     * @return el número de usuarios con el rol especificado
     */
    long countByRole(Roles role);

    /**
     * Verifica si existe un usuario por email (para reset de contraseña)
     *
     * @param email el Email a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmailForReset(Email email);

    /**
     * Buscar por id
     */
    Optional<User> findById(Long id);
}