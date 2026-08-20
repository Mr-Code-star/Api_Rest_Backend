package com.sanuvi.ferova.apirest.iam.infrastructure.hashing.bcrypt;

import com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  Servicio de hashing BCrypt para contraseñas
 *  <p>
 *       Esta interfaz extiende tanto {@link HashingService} como
 *       {@link PasswordEncoder} de Spring Security, proporcionando
 *       una implementación específica de hashing utilizando el
 *       algoritmo BCrypt.
 *  </p>
 */
public interface BCryptHashingService extends HashingService, PasswordEncoder {
    // Esta interfaz combina HashingService y PasswordEncoder
}
