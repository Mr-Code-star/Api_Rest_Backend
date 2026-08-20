package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de punto de entrada para solicitudes no autorizadas.
 *
 * Esta clase implementa la interfaz AuthenticationEntryPoint de Spring Security
 * y se activa cuando un usuario no autenticado intenta acceder a un recurso
 * que requiere autenticación.
 *
 * Es el punto de partida para manejar errores de autenticación en la aplicación.
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

    /**
     * Método que se ejecuta cuando se detecta una solicitud no autorizada.
     *
     * Este método es llamado automáticamente por Spring Security cuando:
     * - Un usuario intenta acceder a un endpoint protegido sin estar autenticado
     * - El token JWT es inválido o ha expirado
     * - Las credenciales son incorrectas
     *
     * @param request La petición HTTP que generó el error de autenticación
     * @param response La respuesta HTTP donde se enviará el error
     * @param authenticationException La excepción que contiene el detalle del error
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        LOGGER.error("Unauthorized request: {}", authenticationException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request detected");
    }
}