package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Email (String value){

    private static final Pattern EMAIL_PATTERN = Pattern.compile("/^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$/");


    public Email {
        if(value == null || value.isEmpty())
            throw new IllegalArgumentException("Email is required");

        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("Invalid email format");

    }
}
