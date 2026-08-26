package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record VerifyResetCodeResource( String email,
                                       String code) {
}
