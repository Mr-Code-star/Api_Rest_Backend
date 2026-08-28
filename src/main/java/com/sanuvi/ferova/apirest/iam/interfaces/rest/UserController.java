package com.sanuvi.ferova.apirest.iam.interfaces.rest;

import com.sanuvi.ferova.apirest.iam.domain.model.queries.*;
import com.sanuvi.ferova.apirest.iam.domain.services.UserQueryService;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.UserResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UserController {
    private final UserQueryService userQueryService;

    public UserController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping(value = "/{userId}")
    @Operation(summary = "Get user by id", description = "Get the user with the given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<UserResource> getUserById(@PathVariable String userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/mothers/search")
    @PreAuthorize("hasAnyRole('NURSE')")
    @Operation(summary = "Search Mothers", description = "Search mothers by name, lastname, full name or DNI (NURSE).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mothers found successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid search term."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Nurse role required.")})
    public ResponseEntity<List<UserResource>> searchMothers(@RequestParam String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        var users = userQueryService.handle(new GetMothersBySearchTermQuery(searchTerm));
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping("/mothers")
    @Operation(summary = "Get All Mothers", description = "Get all mother users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mothers retrieved successfully."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Nurse role required.")})
    public ResponseEntity<List<UserResource>> getMothers() {
        var users = userQueryService.handle(new GetMothersQuery());
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get User by Email", description = "Get a user by their email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Nurse role required.")})
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        var user = userQueryService.handle(new GetUserByEmailQuery(email));

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/dni/{dni}")
    @Operation(summary = "Get User by DNI", description = "Get a user by their DNI.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully."),
            @ApiResponse(responseCode = "404", description = "User not found."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin or Nurse role required.")})
    public ResponseEntity<UserResource> getUserByDni(@PathVariable String dni) {
        var user = userQueryService.handle(new GetUserByDniQuery(dni));

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/staff")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get All Staff Users", description = "Get all staff users (Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Staff users retrieved successfully."),
            @ApiResponse(responseCode = "403", description = "Forbidden - Admin role required.")})
    public ResponseEntity<List<UserResource>> getAllStaffUsers() {
        var users = userQueryService.handle(new GetAllStaffUsersQuery());
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(userResources);
    }

}
