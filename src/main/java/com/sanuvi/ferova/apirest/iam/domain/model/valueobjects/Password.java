package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Password(String value) {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$"
    );

    public Password {
        if (value == null) {
            throw new IllegalArgumentException("Password is required");
        }

        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Password must contain uppercase, lowercase, number and symbol"
            );
        }
    }
}
