package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Constructor de tokens de autenticación para Spring Security
 * <p>
 *     Esta clase proporciona un método estático para construir
 *     {@link UsernamePasswordAuthenticationToken} a partir de un
 *     {@link UserDetails} y una solicitud HTTP, incluyendo los
 *     detalles de la autenticación.
 * </p>
 *
 * <p>
 *     Utilidad para establecer la autenticación en el contexto
 *     de seguridad de Spring después de validar un token JWT.
 * </p>
 *
 * @see UsernamePasswordAuthenticationToken
 * @see UserDetails
 */
public class UsernamePasswordAuthenticationTokenBuilder {


    /**
     * Construye un token de autenticación con los detalles de la solicitud
     *
     * <p>
     *     Este método crea un {@link UsernamePasswordAuthenticationToken}
     *     con el principal (usuario autenticado) y sus autoridades,
     *     y luego establece los detalles de la solicitud HTTP.
     * </p>
     *
     * <p>
     *     El token resultante se utiliza para establecer la autenticación
     *     en el {@link org.springframework.security.core.context.SecurityContextHolder}
     * </p>
     *
     * @param principal el {@link UserDetails} del usuario autenticado
     * @param request la solicitud HTTP actual
     * @return el token de autenticación configurado
     */
    public static UsernamePasswordAuthenticationToken build(
            UserDetails principal,
            HttpServletRequest request) {



        // Crear token con el principal y sus autoridades
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,  // Credenciales (no necesarias después de la autenticación)
                        principal.getAuthorities()
                );

        // Establecer los detalles de la solicitud (IP, sesión, etc.)
        usernamePasswordAuthenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        return usernamePasswordAuthenticationToken;
    }
}