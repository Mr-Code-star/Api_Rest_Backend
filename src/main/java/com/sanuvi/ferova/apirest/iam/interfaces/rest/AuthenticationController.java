package com.sanuvi.ferova.apirest.iam.interfaces.rest;

import com.sanuvi.ferova.apirest.iam.domain.services.UserCommandService;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.LoginUserResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.AuthenticatedUserResourceFromEntityAssembler;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.LoginUserCommandResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Available Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "login with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")})
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody LoginUserResource loginResource) {
        var loginUserCommand = LoginUserCommandResourceFromEntityAssembler.toCommandFromResource(loginResource);
        var authenticatedUser = userCommandService.handle(loginUserCommand);
        if (authenticatedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
        return ResponseEntity.ok(authenticatedUserResource);
    }
}