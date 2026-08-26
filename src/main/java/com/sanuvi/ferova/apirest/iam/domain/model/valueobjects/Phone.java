package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record Phone(String value) {
    // Acepta: +51 987654321, +51987654321, 987654321, 51 987654321
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+51\\s\\d{9}|\\+51\\d{9}|51\\s\\d{9}|51\\d{9}|\\d{9})$"
    );

    // Constructor compacto con validación mejorada
    public Phone {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        String normalized = value.trim().replaceAll("\\s+", " ");

        // Si es solo 9 dígitos, formatear como +51 987654321
        if (normalized.matches("\\d{9}")) {
            normalized = "+51 " + normalized;
        }
        // Si es 51 + 9 dígitos sin espacio
        else if (normalized.matches("51\\d{9}")) {
            normalized = "+51 " + normalized.substring(2);
        }
        // Si es +51 + 9 dígitos sin espacio
        else if (normalized.matches("\\+51\\d{9}")) {
            normalized = "+51 " + normalized.substring(3);
        }

        // Validar formato final
        if (!PHONE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "Phone must follow format: +51 987654321"
            );
        }

        value = normalized;
    }
}