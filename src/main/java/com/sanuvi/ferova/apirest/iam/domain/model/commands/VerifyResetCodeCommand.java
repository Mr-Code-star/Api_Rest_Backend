package com.sanuvi.ferova.apirest.iam.domain.model.commands;

public record VerifyResetCodeCommand(
        String email,
        String code
) {
}
