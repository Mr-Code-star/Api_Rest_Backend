package com.sanuvi.ferova.apirest.iam.domain.model.commands;

public record ResetPasswordCommand (
        String email,
        String code,
        String newPassword
) {}
