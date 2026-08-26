package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.ResetPasswordCommand;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.ResetPasswordResource;

public class ResetPasswordCommandFromEntityAssembler {
    public static ResetPasswordResource ToCommandFromResource(ResetPasswordCommand command){
        return new ResetPasswordResource(command.email(), command.code(), command.newPassword());
    }
}
