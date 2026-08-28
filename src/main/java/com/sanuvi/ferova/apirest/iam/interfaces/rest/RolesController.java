package com.sanuvi.ferova.apirest.iam.interfaces.rest;

import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetAllRolesQuery;
import com.sanuvi.ferova.apirest.iam.domain.services.RoleQueryService;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.RoleResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Role Management Endpoints")
public class RolesController {

    private final RoleQueryService roleQueryService;

    public RolesController( RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Get all the roles available in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")})
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRoleQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRoleQuery);
        var roleResources = roles.stream().map(RoleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(roleResources);
    }
}
