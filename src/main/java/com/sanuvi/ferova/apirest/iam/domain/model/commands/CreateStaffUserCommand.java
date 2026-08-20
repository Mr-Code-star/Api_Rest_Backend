package com.sanuvi.ferova.apirest.iam.domain.model.commands;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;

import java.util.List;

public record CreateStaffUserCommand(
        String name,
        String lastName,
        String dni,
        String email,
        String phone,
        String password,
        List<Role> roles
) { }
