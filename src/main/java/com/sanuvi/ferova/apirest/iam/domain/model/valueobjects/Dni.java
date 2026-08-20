package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Dni (String value) {
    private static final Pattern DNI_PATTERN = Pattern.compile("^\\d{8}$");

    public Dni {

        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("DNI is required");


        if (!DNI_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("DNI must contain exactly 8 numeric digits");
    }
}
