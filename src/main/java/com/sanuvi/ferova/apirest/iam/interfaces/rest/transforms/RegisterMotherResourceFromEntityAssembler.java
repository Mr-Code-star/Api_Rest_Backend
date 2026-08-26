package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.RegisterMotherCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.RegisterMotherResource;

public class RegisterMotherResourceFromEntityAssembler {
    public static RegisterMotherCommand ToCommandFromResource(RegisterMotherResource resource) {
        return new RegisterMotherCommand(
                resource.name(),
                resource.lastName(),
                resource.dni(),
                resource.email(),
                resource.phone(),
                resource.password()
        );
    }
}
