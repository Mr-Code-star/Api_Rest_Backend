package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.RequestResetCodeCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.RequestResetCodeResource;

public class RequestResetCodeCommandFromEntityAssembler {
    public static RequestResetCodeCommand ToCommandFromResource(RequestResetCodeResource resource) {
        return new RequestResetCodeCommand(resource.email());
    }
}
