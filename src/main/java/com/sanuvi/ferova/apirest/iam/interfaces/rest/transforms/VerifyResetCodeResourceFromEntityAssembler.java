package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.VerifyResetCodeCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.VerifyResetCodeResource;

public class VerifyResetCodeResourceFromEntityAssembler {
    public static VerifyResetCodeCommand ToCommandFromResource(VerifyResetCodeResource resource) {
        return new VerifyResetCodeCommand(resource.email(), resource.code());
    }
}
