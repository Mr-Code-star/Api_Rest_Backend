package com.sanuvi.ferova.apirest.iam.domain.model.commands;

public record RegisterMotherCommand (
        String name,
        String lastName,
        String dni,
        String email,
        String phone,
        String password
) {}
