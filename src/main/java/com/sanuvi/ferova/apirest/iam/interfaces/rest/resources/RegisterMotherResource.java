package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record RegisterMotherResource(String name,
                                     String lastName,
                                     String dni,
                                     String email,
                                     String phone,
                                     String password) {
}
