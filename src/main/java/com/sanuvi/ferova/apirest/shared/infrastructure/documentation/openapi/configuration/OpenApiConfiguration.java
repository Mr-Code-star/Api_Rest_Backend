package com.sanuvi.ferova.apirest.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger) para documentación de la API REST.
 * Disponible en: http://localhost:8080/swagger-ui/index.html
 *
 * <p>Configura información general y autenticación JWT (Bearer Token).
 */
@Configuration
public class OpenApiConfiguration {

    // Propiedades

    /**
     * Nombre de la aplicación, inyectado desde application.yml
     */
    @Value("${spring.application.name}")
    String applicationName;

    /**
     * Descripción de la aplicación, inyectado desde application.yml
     */
    @Value("${documentation.application.description}")
    String applicationDescription;

    /**
     * Versión de la aplicación, inyectado desde application.yml
     */
    @Value("${documentation.application.version}")
    String applicationVersion;

    /**
     *  Configura el bean de OpenAPI con toda la información de la documentación.
     *  Este método crea y configura el objeto OpenAPI que será utilizado
     *  por SpringDoc para generar la documentación de la API.
     *  La configuración incluye:
     *   Información general: Título, descripción, versión y licencia
     *   Seguridad: Esquema de autenticación JWT con Bearer Token
     *
     *  @return OpenAPI objeto configurado con toda la documentación
     */

    @Bean
    public OpenAPI FerovaPlatformOpenApi() {

        // Configuracion General
        // Configura la información principal de la API
        // Esto se muestra en la página de Swagger UI
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title(this.applicationName)
                        .description(this.applicationDescription)
                        .version(this.applicationVersion)
                        .license(new License().name("Apache 2.0")));




         // Nombre del esquema de seguridad.
         // Este nombre se referencia en los endpoints para indicar que requieren autenticación.
         // @SecurityRequirement(name = "BeareAuth") en los controladores

        final String securitySchemaName = "BeareAuth";

        openApi.addSecurityItem(new SecurityRequirement()
                .addList(securitySchemaName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemaName,
                                new SecurityScheme()
                                        .name(securitySchemaName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

        // Retorna el objeto OpenAPI con todas las configuraciones aplicadas

        return openApi;
    }
}
