package com.sanuvi.ferova.apirest.iam.interfaces.rest;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.ResetPasswordCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.commands.VerifyResetCodeCommand;
import com.sanuvi.ferova.apirest.iam.domain.services.UserCommandService;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.RequestResetCodeResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.VerifyResetCodeResource;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms.RequestResetCodeCommandFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/password", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Password Reset", description = "Password Reset Endpoints")
public class ResetPasswordController {
    private final UserCommandService userCommandService;

    public ResetPasswordController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/reset/request")
    @Operation(summary = "Request Reset Code", description = "Request a password reset code to be sent to the user's email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset code sent successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid email or user not found.")})
    public ResponseEntity<Map<String, String>> requestResetCode(@Valid @RequestBody RequestResetCodeResource requestResetCodeResource) {

        var requestResetCodeCommand =  RequestResetCodeCommandFromEntityAssembler.ToCommandFromResource(requestResetCodeResource);

        var result = userCommandService.handle(requestResetCodeCommand);

        if(result.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/reset/verify")
    @Operation(summary = "Verify Reset Code", description = "Verify the password reset code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset code is valid."),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset code.")})
    public ResponseEntity<Map<String, String>> verifyResetCode(@Valid @RequestBody VerifyResetCodeResource verifyResetCodeResource) {
        var verifyResetCodeCommand = new VerifyResetCodeCommand(verifyResetCodeResource.code(), verifyResetCodeResource.email());

        var result = userCommandService.handle(verifyResetCodeCommand);

        if (result.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/reset")
    @Operation(summary = "Reset Password", description = "Reset the user's password after code verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid or expired reset code.")})
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordCommand resetPasswordResource) {

        var resetPasswordCommand = new ResetPasswordCommand(resetPasswordResource.email(), resetPasswordResource.code(), resetPasswordResource.newPassword());

        var result = userCommandService.handle(resetPasswordCommand);

        if(result.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.noContent().build();

    }


}
