package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Phone(String value) {
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+51\\s\\d{9}$");
    private static final Pattern DIGITS_ONLY =
            Pattern.compile("\\d+");

    // Constructor compacto con validación
    public Phone {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        // Validar formato antes de normalizar
        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Phone must follow format: +51 987654321"
            );
        }

        // Normalizar: eliminar todo excepto dígitos
        value = normalize(value);
    }

    private static String normalize(String phone) {
        return phone.replaceAll("\\D", "");
    }

    public static Phone fromPersistence(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        // Desde persistencia solo deben venir dígitos
        if (!phone.matches("^\\d+$")) {
            throw new IllegalArgumentException(
                    "Phone from persistence must contain only digits"
            );
        }

        return new Phone("+51 " + phone.substring(0, 3) + " " + phone.substring(3));

    }

}
