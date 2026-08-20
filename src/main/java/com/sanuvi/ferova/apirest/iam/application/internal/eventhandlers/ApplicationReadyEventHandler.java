package com.sanuvi.ferova.apirest.iam.application.internal.eventhandlers;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.SeedRolesCommand;
import com.sanuvi.ferova.apirest.iam.domain.services.RoleCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Manejador del evento ApplicationReadyEvent
 * <p>
 *     Esta clase se ejecuta automáticamente cuando la aplicación Spring Boot
 *     ha completado su inicio y está lista para recibir solicitudes.
 * </p>
 */
@Service
public class ApplicationReadyEventHandler {
    private final RoleCommandService roleCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);

    public ApplicationReadyEventHandler(RoleCommandService roleCommandService) {
        this.roleCommandService = roleCommandService;
    }

    /**
     * Maneja el evento ApplicationReadyEvent
     * <p>
     *     Este método se ejecuta automáticamente después de que la aplicación
     *     ha iniciado completamente y verifica si es necesario sembrar los roles.
     * </p>
     *
     * @param event el evento ApplicationReadyEvent
     */
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting to verify if roles seeding is needed for {} at {}", applicationName, currentTimestamp());
        var seedRolesCommand = new SeedRolesCommand();
        roleCommandService.handle(seedRolesCommand);
        LOGGER.info("Roles seeding verification finished for {} at {}", applicationName, currentTimestamp());
    }

    /**
     * Obtiene el timestamp actual
     *
     * @return timestamp actual
     */
    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
