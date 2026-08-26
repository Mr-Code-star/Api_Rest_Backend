package com.sanuvi.ferova.apirest.shared.infrastructure.security;

import com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Servicio para obtener información del usuario autenticado actualmente
 * <p>
 *     Esta clase proporciona métodos utilitarios para acceder a la información
 *     del usuario autenticado en el contexto de seguridad de Spring.
 * </p>
 *
 * <p>
 *     Utilidades proporcionadas:
 *     - Obtener ID del usuario autenticado
 *     - Obtener DNI del usuario autenticado
 *     - Verificar si hay un usuario autenticado
 *     - Obtener el objeto UserDetails completo
 * </p>
 */
@Component
public class AuthenticationHelper {

    /**
     * Obtiene el ID del usuario autenticado actualmente
     *
     * <p>
     *     Este método extrae el ID del usuario del objeto {@link UserDetailsImpl}
     *     almacenado en el contexto de seguridad de Spring.
     * </p>
     *
     * @return el ID del usuario, o null si no hay usuario autenticado
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }

        return null;
    }

    /**
     * Obtiene el DNI del usuario autenticado actualmente
     *
     * <p>
     *     Este método extrae el DNI del usuario del objeto {@link UserDetailsImpl}
     *     almacenado en el contexto de seguridad de Spring.
     * </p>
     *
     * @return el DNI del usuario, o null si no hay usuario autenticado
     */
    public String getCurrentDni() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getUsername(); // DNI es el username
        }

        return null;
    }

    /**
     * Obtiene el nombre completo del usuario autenticado actualmente
     *
     * @return el nombre completo del usuario, o null si no hay usuario autenticado
     */
    public String getCurrentFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getFullName();
        }

        return null;
    }

    /**
     * Obtiene el objeto UserDetails del usuario autenticado actualmente
     *
     * @return el {@link UserDetailsImpl} del usuario, o null si no hay usuario autenticado
     */
    public UserDetailsImpl getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails;
        }

        return null;
    }

    /**
     * Verifica si hay un usuario autenticado actualmente
     *
     * @return true si hay un usuario autenticado, false en caso contrario
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetailsImpl;
    }

    /**
     * Obtiene el rol del usuario autenticado actualmente
     *
     * @return el rol del usuario, o null si no hay usuario autenticado
     */
    public String getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority())
                    .orElse(null);
        }

        return null;
    }

    /**
     * Verifica si el usuario autenticado tiene un rol específico
     *
     * @param role el rol a verificar (ej: "ROLE_ADMIN")
     * @return true si el usuario tiene el rol, false en caso contrario
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}