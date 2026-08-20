package com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.tokens;

/**
 * Servicio para manejar Tokens
 *
 *      Esta interfaz define las operaciones necesarias para generar, validar
 *      y extraer información de tokens JWT utilizados para la autenticación
 *      y autorización de usuarios en la aplicación.
 *
 *      Los tokens JWT son utilizados para:
 *        - Autenticación stateless en APIs REST
 *        - Manejo de sesiones sin estado (sin necesidad de almacenar en servidor)
 *        - Transmisión segura de información del usuario
 *        - Control de expiración de sesiones
 */
public interface TokenService {

    /**
     * Genera un token JWT para un usuario basado en su DNI
     *
     *   El token generado contiene:
     *     - Subject: DNI del usuario
     *     - IssuedAt: Fecha de emisión
     *     - Expiration: Fecha de expiración (configurable)
     *     - Claims adicionales (roles, permisos, etc.)
     *
     * @param dni el DNI del usuario para el cual se genera el token
     * @return un String con el token JWT generado
     */
    String generateToken(String dni);

    /**
     * Extrae el nombre de usuario (DNI) desde un token JWT
     *
     * <p>
     *     Este método parsea el token y extrae el subject (DNI) del payload.
     *     Utilizado principalmente para identificar al usuario en cada solicitud.
     * </p>
     *
     * @param token el token JWT del cual extraer el nombre de usuario
     * @return el DNI del usuario contenido en el token
     */

    String getUsernameFromToken(String token);

    /**
     * Valida un token JWT
     *     Este método es utilizado en los filtros de seguridad para
     *     validar cada solicitud entrante que contenga un token JWT.
     *
     * @param token el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     *         (incluye tokens expirados, mal formados o con firma inválida)
     */
    boolean validateToken(String token);
}
