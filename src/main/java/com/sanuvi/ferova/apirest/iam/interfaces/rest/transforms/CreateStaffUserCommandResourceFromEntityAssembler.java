package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.CreateStaffUserCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.CreateStaffUserResource;

public class CreateStaffUserCommandResourceFromEntityAssembler {
    public static CreateStaffUserCommand toCommandFromResource(CreateStaffUserResource resource) {
        return new CreateStaffUserCommand(resource.name(), resource.lastName(), resource.dni(), resource.email(), resource.phone(), resource.password(), resource.roleId());
    }
}
