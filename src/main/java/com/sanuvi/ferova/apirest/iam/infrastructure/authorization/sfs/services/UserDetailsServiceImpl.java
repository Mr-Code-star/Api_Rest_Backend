package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.services;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Dni;
import com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Implementación del servicio de detalles de usuario para Spring Security.
 *
 * Esta clase es responsable de cargar los datos de un usuario desde la base de datos
 * utilizando su DNI (Documento Nacional de Identidad) como identificador único.
 *
 * El nombre del bean "defaultUserDetailsService" se usa para referenciarlo
 * en otros componentes como WebSecurityConfiguration.
 */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param userRepository Repositorio de usuarios para operaciones de base de datos
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga los detalles de un usuario por su nombre de usuario (DNI).
     *
     * Este método es llamado por Spring Security durante el proceso de autenticación
     * para obtener los datos del usuario que está intentando autenticarse.
     *
     * Proceso:
     * 1. Convierte el username (String) en un Value Object DNI
     * 2. Busca el usuario en la base de datos usando el DNI
     * 3. Si no se encuentra, lanza una excepción UsernameNotFoundException
     * 4. Si se encuentra, construye un objeto UserDetails con los datos del usuario
     *
     * @param username El DNI del usuario como String (se espera que sea un DNI válido)
     * @return UserDetails con la información del usuario para Spring Security
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el DNI proporcionado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Crear Value Object DNI usando el constructor
        Dni dni = new Dni(username);

        // Buscar usuario por DNI
        User user = userRepository.findByDni(dni).orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con DNI: " + dni
        ));

        return UserDetailsImpl.build(user);
    }
}
