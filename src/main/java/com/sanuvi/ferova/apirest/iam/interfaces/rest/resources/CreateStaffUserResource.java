package com.sanuvi.ferova.apirest.iam.interfaces.rest.resources;

public record CreateStaffUserResource(String name,
                                      String lastName,
                                      String dni,
                                      String email,
                                      String phone,
                                      String password,
                                      Long roleId) {
}
