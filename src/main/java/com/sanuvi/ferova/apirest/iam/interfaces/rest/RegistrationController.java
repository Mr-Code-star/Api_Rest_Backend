package com.sanuvi.ferova.apirest.iam.interfaces.rest;

import com.sanuvi.ferova.apirest.iam.domain.services.UserCommandService;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.CreateStaffUserResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.RegisterMotherResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.UserResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.CreateStaffUserCommandResourceFromEntityAssembler;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.RegisterMotherResourceFromEntityAssembler;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de registro de usuarios
 * <p>
 *     Este controlador maneja los endpoints relacionados con el registro
 *     de nuevos usuarios en el sistema.
 * </p>
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/api/v1/registration", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Registration", description = "User Registration Endpoints")
public class RegistrationController {

    private final UserCommandService userCommandService;

    public RegistrationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Registra una nueva madre en el sistema
     *
     * @param registerMotherResource los datos de la madre a registrar
     * @return el usuario creado con código HTTP 201 (Created)
     */
    @PostMapping("/mothers")
    @Operation(summary = "Register Mother", description = "Register a new mother user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mother registered successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "409", description = "User already exists.")})
    public ResponseEntity<UserResource> registerMother(@Valid @RequestBody RegisterMotherResource registerMotherResource) {
        var registerMotherCommand = RegisterMotherResourceFromEntityAssembler.ToCommandFromResource(registerMotherResource);
        var user = userCommandService.handle(registerMotherCommand);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResource);
    }

    /**
     * Crea un usuario staff (Administrador o Enfermera)
     *
     * @param createStaffUserResource los datos del usuario staff a crear
     * @return el usuario creado con código HTTP 201 (Created)
     */
    @PostMapping("/staff")
    @Operation(summary = "Create Staff User", description = "Create a new staff user (Admin or Nurse).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Staff user created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "409", description = "User already exists.")})
    public ResponseEntity<UserResource> createStaffUser(@Valid @RequestBody CreateStaffUserResource createStaffUserResource) {
        var createStaffUserCommand = CreateStaffUserCommandResourceFromEntityAssembler.toCommandFromResource(createStaffUserResource);
        var user = userCommandService.handle(createStaffUserCommand);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResource);
    }
}