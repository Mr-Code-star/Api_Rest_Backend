package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(user.getId(), user.getFullName(), user.getRole().getId(), token);
    }
}
