package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(String id, String fullName, String roleId, String token) {
}
