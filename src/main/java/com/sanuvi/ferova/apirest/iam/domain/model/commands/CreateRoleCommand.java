package com.sanuvi.ferova.apirest.iam.domain.model.commands;

import java.util.List;

public record CreateRoleCommand(
        String name,
        String displayName,
        String description,
        Integer level,
        List<String> permissions
) { }