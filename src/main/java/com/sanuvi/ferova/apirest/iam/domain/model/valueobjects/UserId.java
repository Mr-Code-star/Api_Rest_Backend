package com.sanuvi.ferova.apirest.iam.domain.model.valueobjects;

public record UserId(String value) {

    public UserId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("User id is required");
        }
    }

    // Factory method para creación
    public static UserId of(String userId) {
        return new UserId(userId);
    }

    // Factory method para generar IDs automáticos (UUID)
    public static UserId generate() {
        return new UserId(java.util.UUID.randomUUID().toString());
    }

    // Métodos de utilidad
    public boolean isValid() {
        return value != null && !value.isBlank();
    }
}