package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Password(String value) {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$"
    );

    public Password {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    private static String normalize(String phone) {
        return phone.replaceAll("\\D", "");
    }
}
