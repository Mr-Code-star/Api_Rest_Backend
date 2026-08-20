package com.sanuvi.ferova.apirest.iam.domain.model.commands;

public record LoginUserCommand(
   String dni, String password
) {}
