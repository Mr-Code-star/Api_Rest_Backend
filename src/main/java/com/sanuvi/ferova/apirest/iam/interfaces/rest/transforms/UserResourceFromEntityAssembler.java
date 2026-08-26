package com.sanuvi.ferova.apirest.iam.interfaces.rest.transforms;

import com.sanuvi.ferova.apirest.iam.domain.model.aggregate.User;
import com.sanuvi.ferova.apirest.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(user.getId(), user.getFullName(), user.getRole().getId());
    }
}
