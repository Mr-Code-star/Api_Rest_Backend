package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementación de UserDetails para Spring Security
 * <p>
 *     Esta clase adapta la entidad {@link User} al modelo de seguridad
 *     de Spring Security, proporcionando la información necesaria para
 *     la autenticación y autorización.
 * </p>
 * @see UserDetails
 * @see User
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

    /**
     * ID del usuario (de MongoDB)
     */
    private final String id;

    /**
     * DNI del usuario (username para Spring Security)
     */
    private final String dni;

    /**
     * Nombre completo del usuario
     */
    private final String fullName;

    /**
     * Contraseña hasheada del usuario
     */
    @JsonIgnore
    private final String password;

    /**
     * Estado de la cuenta
     */
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    /**
     * Autoridades (roles) del usuario
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor privado para construir UserDetailsImpl
     *
     * @param id ID del usuario
     * @param dni DNI del usuario
     * @param fullName nombre completo del usuario
     * @param password contraseña hasheada
     * @param authorities roles del usuario
     */
    private UserDetailsImpl(String id, String dni, String fullName, String password,
                            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.dni = dni;
        this.fullName = fullName;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }

    /**
     * Construye un UserDetailsImpl a partir de un User
     *
     * <p>
     *     Este método convierte la entidad User del dominio al modelo
     *     de seguridad de Spring Security, extrayendo:
     *     - ID del usuario
     *     - DNI como username
     *     - Nombre completo
     *     - Contraseña hasheada
     *     - Rol como autoridad (ROLE_ADMIN, ROLE_NURSE, ROLE_MOTHER)
     * </p>
     *
     * @param user el usuario del dominio
     * @return UserDetailsImpl adaptado para Spring Security
     */
    public static UserDetailsImpl build(User user) {
        // Crear autoridad con el rol del usuario (con prefijo ROLE_)
        GrantedAuthority authority = new SimpleGrantedAuthority(
                "ROLE_" + user.getRole().getStringName()
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getDni().value(),
                user.getFullName(),
                user.getPassword().value(),
                List.of(authority)
        );
    }

    /**
     * Obtiene las autoridades del usuario
     *
     * @return colección de autoridades (roles)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Obtiene la contraseña del usuario
     *
     * @return contraseña hasheada
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el nombre de usuario (DNI)
     * Método requerido por UserDetails
     *
     * @return DNI del usuario
     */
    @Override
    public String getUsername() {
        return dni;
    }

    /**
     * Verifica si la cuenta no ha expirado
     *
     * @return true siempre (puede personalizarse)
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Verifica si la cuenta no está bloqueada
     *
     * @return true siempre (puede personalizarse)
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Verifica si las credenciales no han expirado
     *
     * @return true siempre (puede personalizarse)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * Verifica si la cuenta está habilitada
     *
     * @return true siempre (puede personalizarse)
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}