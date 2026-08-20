package com.sanuvi.ferova.apirest.iam.infrastructure.hashing.bcrypt.services;

import com.sanuvi.ferova.apirest.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Implementación del servicio de hashing BCrypt
 * <p>
 *     Esta clase implementa la interfaz {@link BCryptHashingService} utilizando
 *     {@link BCryptPasswordEncoder} de Spring Security para el hashing de contraseñas.
 * </p>
 *
 * <p>
 *     Características principales:
 *     - Utiliza BCryptPasswordEncoder con factor de costo por defecto (10)
 *     - Proporciona hashing seguro con salting automático
 *     - Verificación de contraseñas con comparación resistente a timing attacks
 * </p>
 */
public class HashingServiceImpl implements BCryptHashingService {

    /**
     * PasswordEncoder BCrypt de Spring Security
     * Inicializado con factor de costo por defecto (10)
     */
    private final BCryptPasswordEncoder passwordEncoder;


    /**
     * Constructor que inicializa el encoder BCrypt con factor de costo por defecto
     *
     * <p>
     *     El factor de costo 10 significa 2^10 = 1024 iteraciones,
     *     que es un buen balance entre seguridad y rendimiento.
     * </p>
     */
    HashingServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     *     Codifica una contraseña en texto plano utilizando BCrypt.
     *     El proceso incluye:
     *     1. Generación automática de una sal aleatoria
     *     2. Aplicación del algoritmo BCrypt con factor de costo 10
     *     3. Retorno del hash en formato: $2a$10$salt$hash
     * </p>
     *
     * @param rawPassword la contraseña en texto plano a codificar
     * @return el hash BCrypt de la contraseña
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Verifica si una contraseña en texto plano coincide con su hash BCrypt.
     *     El proceso incluye:
     *     1. Extracción de la sal del hash almacenado
     *     2. Aplicación del mismo algoritmo y sal a la contraseña ingresada
     *     3. Comparación segura contra ataques de timing
     * </p>
     *
     * @param rawPassword la contraseña en texto plano a verificar
     * @param encodedPassword el hash BCrypt almacenado
     * @return true si la contraseña coincide con el hash, false en caso contrario
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
