package com.sanuvi.ferova.apirest.iam.domain.model.commands;

import java.util.List;

public record UpdateRoleCommand(
        String id,
        String displayName,
        String description,
        Integer level,
        List<String> permissions,
        Boolean isActive
) { }