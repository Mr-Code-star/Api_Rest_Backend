package com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.configuration;

import com.sanuvi.ferova.apirest.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.sanuvi.ferova.apirest.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import com.sanuvi.ferova.apirest.iam.infrastructure.tokens.jwt.BearerTokenService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuración principal de seguridad web para la aplicación.
 * Define los componentes y reglas de autenticación y autorización.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final UserDetailsService userDetailsService;

    private final BearerTokenService tokenService;

    private final BCryptHashingService hashingService;

    private final AuthenticationEntryPoint unauthorizedRequestHandler;

    /**
     * Constructor con inyección de dependencias.
     * Se usa @Qualifier para seleccionar la implementación específica de UserDetailsService.
     */
    public WebSecurityConfiguration(@Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService, BearerTokenService tokenService, BCryptHashingService hashingService, AuthenticationEntryPoint authenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.hashingService = hashingService;
        this.unauthorizedRequestHandler = authenticationEntryPoint;
    }

    /**
     * Filtro personalizado que intercepta las peticiones para validar tokens JWT
     * presentes en el encabezado Authorization.
     */
    @Bean
    public BearerAuthorizationRequestFilter authorizationRequestFilter() {
        return new BearerAuthorizationRequestFilter(tokenService, userDetailsService);
    }

    /**
     * Provee el gestor de autenticación que Spring Security usa para validar credenciales.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Proveedor de autenticación basado en DAO que usa UserDetailsService y
     * el codificador de contraseñas BCrypt para validar usuarios.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(hashingService);
        return authenticationProvider;
    }

    /**
     * Expone el codificador de contraseñas como bean para ser usado en otros componentes.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return hashingService;
    }

    /**
     * Define la cadena de filtros de seguridad:
     * - Configuración CORS permisiva (todos los orígenes, métodos y cabeceras)
     * - Desactiva CSRF (stateless)
     * - Manejo de excepciones con entry point personalizado
     * - Sesión sin estado (STATELESS) para usar JWT
     * - Autorización: actualmente permite todas las peticiones (permitAll)
     *
     * NOTA: El filtro de autorización JWT está comentado para pruebas.
     * Para activarlo, descomentar la línea:
     * http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(configurer -> configurer.configurationSource(_ -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("*"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        }));
        http.csrf(csrfConfigurer -> csrfConfigurer.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(unauthorizedRequestHandler))
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class); // Desactivado para pruebas
        return http.build();
    }
}