package com.sanuvi.ferova.apirest.iam.domain.model.commands;

import com.sanuvi.ferova.apirest.iam.domain.model.enumeration.Role;

public record CreateStaffUserCommand(
        String name,
        String lastName,
        String dni,
        String email,
        String phone,
        String password,
        Role role
) { }
