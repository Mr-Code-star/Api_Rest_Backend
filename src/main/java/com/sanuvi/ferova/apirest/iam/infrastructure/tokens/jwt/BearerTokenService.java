package com.sanuvi.ferova.apirest.iam.infrastructure.tokens.jwt;

import com.sanuvi.ferova.apirest.iam.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Servicio para manejar tokens JWT en formato Bearer
 * <p>
 *     Esta interfaz extiende {@link TokenService} y proporciona
 *     funcionalidades específicas para el manejo de tokens Bearer
 *     utilizados en autenticación mediante el header Authorization.
 * </p>
 *
 *  * <p>
 *  *     Los tokens Bearer son utilizados en:
 *  *     - Autenticación stateless en APIs REST
 *  *     - Header Authorization: "Bearer {token}"
 *  *     - Solicitudes autenticadas desde clientes
 *  *     - Protección de endpoints con Spring Security
 *  * </p>
 */
public interface BearerTokenService extends TokenService {

    /**
     * Genera un token JWT a partir de la autenticación de Spring Security
     *
     * <p>
     *     Este método crea un token utilizando la información del objeto
     *     {@link Authentication} de Spring Security, que contiene:
     *     - Principal: El usuario autenticado
     *     - Credentials: Las credenciales del usuario
     *     - Authorities: Los roles y permisos del usuario
     *     - Details: Información adicional de la autenticación
     * </p>
     * @param authentication el objeto {@link Authentication} con los datos del usuario autenticado
     * @return un String con el token JWT generado
     */
    String generateToken(Authentication authentication);

    /**
     * Extrae el token Bearer desde la solicitud HTTP
     *
     * <p>
     *     Este método obtiene el token JWT del header Authorization
     *     de la solicitud HTTP, específicamente del formato:
     *     <pre>
     *         Authorization: Bearer {token}
     *     </pre>
     * </p>
     *
     * <p>
     *     Realiza las siguientes validaciones:
     *     1. Verifica que el header Authorization exista
     *     2. Valida que comience con "Bearer "
     *     3. Extrae el token eliminando el prefijo "Bearer "
     *     4. Retorna el token limpio para su validación
     * </p>
     *
     * @param request el {@link HttpServletRequest} que contiene la solicitud HTTP
     * @return el token JWT sin el prefijo "Bearer ", o null si no está presente o es inválido
     */
    String getBearerTokenFrom(HttpServletRequest request);

}
