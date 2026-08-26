package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.LoginUserCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.LoginUserResource;

public class LoginUserCommandResourceFromEntityAssembler {
    public static LoginUserCommand toCommandFromResource(LoginUserResource resource) {
        return new LoginUserCommand(resource.dni(), resource.password());
    }
}
