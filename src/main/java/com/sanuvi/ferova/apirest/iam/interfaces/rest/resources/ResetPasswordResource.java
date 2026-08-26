package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record ResetPasswordResource(
        String email,
        String code,
        String newPassword
) { }
