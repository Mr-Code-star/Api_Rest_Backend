package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String fullName, Long roleId, String token) {
}
