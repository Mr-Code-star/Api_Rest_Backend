package com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects;

import java.time.LocalDate;

/**
 * Value Object para la fecha de nacimiento
 * <p>
 *     Representa la fecha de nacimiento de una persona.
 *     Valida que la fecha no sea futura.
 * </p>
 *
 * @version 1.0
 */
public record BirthDate(LocalDate value) {
    public BirthDate {
        if (value == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
    }

    /**
     * Valida que la fecha de nacimiento no sea futuro
     * @param date
     */
    private void ensureIsValid(LocalDate date) {
        LocalDate today = LocalDate.now();
        if (date.isAfter(today)) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }

    /*
     * Obtiene la fecha de nacimiento como LocalDate
     */
    public LocalDate getValue() {
       return  value;
    }

    /**
     * Obtiene la fecha de nacimiento como String en formato ISO
     *
     * @return la fecha en formato yyyy-MM-dd
     */
    public String asString() {
        return value.toString();
    }

    /**
     * Calcula la edad actual de la persona
     *
     * @return la edad en años
     */
    public int calculateAge() {
        LocalDate today = LocalDate.now();
        return today.getYear() - value.getYear();
    }

    /**
     * Verifica si la persona es menor de edad (menos de 18 años)
     *
     * @return true si es menor de edad, false en caso contrario
     */
    public boolean isMinor() {

        return !(calculateAge() >= 18);
    }

    /**
     * Factory method para crear desde String
     *
     * @param dateStr la fecha en formato yyyy-MM-dd
     * @return una instancia de BirthDate
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static BirthDate fromString(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException("Birth date is required");
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return new BirthDate(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd");
        }
    }

    @Override
    public String toString() {
        return asString();
    }

}
