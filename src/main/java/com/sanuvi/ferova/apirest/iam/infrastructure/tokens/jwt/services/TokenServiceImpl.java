package com.sanuvi.ferova.apirest.iam.infrastructure.tokens.jwt.services;

import com.sanuvi.ferova.apirest.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Implementación del servicio de tokens JWT para autenticación Bearer
 * <p>
 *     Esta clase implementa la interfaz {@link BearerTokenService} y proporciona
 *     la funcionalidad completa para generar, validar y extraer tokens JWT
 *     utilizados en la autenticación de la aplicación.
 * </p>
 *
 * <p>
 *     Características principales:
 *     - Generación de tokens JWT con firma HMAC-SHA
 *     - Validación de tokens (firma, expiración, formato)
 *     - Extracción de tokens del header Authorization
 *     - Manejo de diferentes tipos de excepciones JWT
 *     - Logging detallado de operaciones
 * </p>
 */
@Service
public class TokenServiceImpl implements BearerTokenService {

    // ===== CONSTANTES ======

    /**
     * Logger para registrar eventos y errores del servicio
     */
    private final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    /**
     * Nombre del header HTTP para la autorización
     */
    private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";

    /**
     * Prefijo para tokens Bearer en el header Authorization
     */
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * Índice donde comienza el token después del prefijo "Bearer "
     * (longitud de "Bearer " = 7 caracteres)
     */
    private static final int TOKEN_BEGIN_INDEX = 7;

    // ===== CAMPOS DE CONFIGURACIÓN =====

    /**
     * Clave secreta para firmar los tokens JWT
     * Configurada en application.yml como: authorization.jwt.secret
     */
    private final String secret;

    /**
     * Días de expiración del token
     * Configurado en application.yml como: authorization.jwt.expiration.days
     */
    private final int expirationDays;

    /**
     * Constructor con inyección de configuración
     *
     * @param secret la clave secreta para firmar tokens
     * @param expirationDays los días hasta la expiración del token
     */
    public TokenServiceImpl(
            @Value("${authorization.jwt.secret}") String secret,
            @Value("${authorization.jwt.expiration.days}") int expirationDays) {
        this.secret = secret;
        this.expirationDays = expirationDays;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Genera un token JWT a partir de la autenticación de Spring Security.
     *     Utiliza el nombre del usuario autenticado como subject del token.
     * </p>
     *
     * @param authentication el objeto {@link Authentication} con los datos del usuario
     * @return el token JWT generado
     */
    @Override
    public String generateToken(Authentication authentication) {
        return buildTokenWithDefaultParameters(authentication.getName());
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Extrae el token JWT del header Authorization de la solicitud HTTP.
     *     El header debe tener el formato: "Authorization: Bearer {token}"
     * </p>
     *
     * @param request la solicitud HTTP
     * @return el token JWT sin el prefijo "Bearer ", o null si no está presente
     */
    @Override
    public String getBearerTokenFrom(HttpServletRequest request) {
        String parameter = getAuthorizationParameterFrom(request);
        if (isTokenPresentIn(parameter) && isBearerTokenIn(parameter))
            return extractTokenFrom(parameter);
        return null;
    }


    /**
     * {@inheritDoc}
     *
     * <p>
     *     Genera un token JWT para un DNI específico.
     *     Utilizado para autenticación directa sin contexto de Spring Security.
     * </p>
     *
     * @param dni el DNI del usuario
     * @return el token JWT generado
     */
    @Override
    public String generateToken(String dni) {
        return buildTokenWithDefaultParameters(dni);
    }

    /**
    * {@inheritDoc}
    *
    * <p>
    *     Extrae el nombre de usuario (DNI) del token JWT.
    *     El subject del token contiene el identificador del usuario.
    * </p>
    *
    * @param token el token JWT
    * @return el DNI del usuario contenido en el token
    */
    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     Valida un token JWT verificando:
     *     1. Firma del token (integridad)
     *     2. Formato del token (estructura JWT)
     *     3. Fecha de expiración (no expirado)
     *     4. Compatibilidad del algoritmo de firma
     * </p>
     *
     * @param token el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            LOGGER.info("Token is valid");
            return true;
        }  catch (SignatureException e) {
            LOGGER.error("Invalid JSON Web Token Signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JSON Web Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    // ===== MÉTODOS PRIVADOS =====

    /**
     * Construye un token JWT con los parámetros por defecto
     *
     * <p>
     *     Configuración del token:
     *     - Subject: DNI del usuario
     *     - IssuedAt: Fecha y hora actual
     *     - Expiration: Fecha actual + días de expiración configurados
     *     - Firma: HMAC-SHA con la clave secreta
     * </p>
     *
     * @param dni el DNI del usuario (subject del token)
     * @return el token JWT generado
     */
    private String buildTokenWithDefaultParameters(String dni) {
        var issuedAt = new Date();
        var expiration = DateUtils.addDays(issuedAt, expirationDays);
        var key = getSigningKey();
        return Jwts.builder()
                .subject(dni)                              // DNI del usuario
                .issuedAt(issuedAt)                        // Fecha de emisión
                .expiration(expiration)                    // Fecha de expiración
                .signWith(key)                             // Firma con clave secreta
                .compact();                                // Construir el token
    }

    /**
     * Extrae un claim específico del token JWT
     *
     * <p>
     *     Utiliza una función para extraer un claim específico del token,
     *     permitiendo extraer diferentes claims de forma genérica.
     * </p>
     *
     * @param token el token JWT
     * @param claimsResolvers la función que resuelve el claim deseado
     * @param <T> el tipo del claim a extraer
     * @return el valor del claim extraído
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT
     *
     * <p>
     *     Parsea el token, verifica la firma y retorna todos los claims
     *     contenidos en el payload del JWT.
     * </p>
     *
     * @param token el token JWT a parsear
     * @return los claims del token
     * @throws JwtException si ocurre un error al parsear el token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    /**
     * Obtiene la clave de firma para los tokens JWT
     *
     * <p>
     *     Convierte la clave secreta en un objeto SecretKey para
     *     ser utilizado en la firma y verificación de tokens.
     * </p>
     *
     * @return la clave secreta para firmar tokens
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Verifica si el token está presente en el header
     *
     * @param authorizationParameter el valor del header Authorization
     * @return true si el header tiene contenido, false en caso contrario
     */
    private boolean isTokenPresentIn(String authorizationParameter) {
        return StringUtils.hasText(authorizationParameter);
    }

    /**
     * Verifica si el header contiene un token Bearer
     *
     * @param authorizationParameter el valor del header Authorization
     * @return true si comienza con "Bearer ", false en caso contrario
     */
    private boolean isBearerTokenIn(String authorizationParameter) {
        return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
    }

    /**
     * Extrae el token del header eliminando el prefijo "Bearer "
     *
     * @param authorizationHeaderParameter el valor del header Authorization
     * @return el token sin el prefijo "Bearer "
     */
    private String extractTokenFrom(String authorizationHeaderParameter) {
        return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
    }

    /**
     * Obtiene el header Authorization de la solicitud HTTP
     *
     * @param request la solicitud HTTP
     * @return el valor del header Authorization, o null si no existe
     */
    private String getAuthorizationParameterFrom(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
    }
}