package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.pipeline;

import com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.sanuvi.ferova.apirest.iam.infrastructure.tokens.jwt.BearerTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta cada petición HTTP una sola vez para validar
 * tokens JWT de tipo Bearer y establecer la autenticación en el contexto de seguridad.
 *
 * Este filtro se ejecuta antes de que la petición llegue a los controladores.
 */
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
    private final BearerTokenService tokenService;

    @Qualifier("defaultUserDetailsService")
    private final UserDetailsService userDetailsService;

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param tokenService Servicio para operaciones con tokens JWT
     * @param userDetailsService Servicio para cargar detalles de usuario desde la base de datos
     */
    public BearerAuthorizationRequestFilter(BearerTokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal del filtro que se ejecuta en cada petición.
     *
     * Proceso:
     * 1. Extrae el token JWT del encabezado Authorization
     * 2. Si el token existe y es válido:
     *    a. Obtiene el nombre de usuario del token
     *    b. Carga los detalles del usuario desde la base de datos
     *    c. Establece la autenticación en el contexto de Spring Security
     * 3. Si el token no es válido, lo registra en el log
     * 4. Continúa con la cadena de filtros
     *
     * @param request Petición HTTP entrante
     * @param response Respuesta HTTP saliente
     * @param filterChain Cadena de filtros para continuar el procesamiento
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Extraer el token del encabezado Authorization
            String token = tokenService.getBearerTokenFrom(request);
            // 2. Validar el token si existe
            LOGGER.info("Token: {}", token);
            if (token != null && tokenService.validateToken(token)) {
                // 3. Obtener el username desde el token
                String username = tokenService.getUsernameFromToken(token);
                // 4. Cargar los detalles del usuario desde la base de datos
                var userDetails = userDetailsService.loadUserByUsername(username);
                // 5. Establecer la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
            } else {
                LOGGER.info("Token is not valid");
            }

        } catch (Exception e) {
            LOGGER.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}