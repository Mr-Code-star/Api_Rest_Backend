package com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects;

import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.WeightCategory;

public record Weight(Double value) {
    /**
     * Peso mínimo en kilogramos (0.5 kg - bebé prematuro)
     */
    private static final double MIN_WEIGHT = 0.5;

    /**
     * Peso máximo en kilogramos (350 kg - máximo realista)
     */
    private static final double MAX_WEIGHT = 350.0;

    /**
     * Constructor compacto con validación
     *
     * @param value el peso en kilogramos
     * @throws IllegalArgumentException si el peso es nulo, menor a 0.5 kg o mayor a 350 kg
     */
    public Weight {
        if (value == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        if (value < MIN_WEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Weight must be at least %.1f kg (minimum for premature babies)", MIN_WEIGHT)
            );
        }
        if (value > MAX_WEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Weight must not exceed %.1f kg", MAX_WEIGHT)
            );
        }
    }

    /**
     * Obtiene el peso en kilogramos
     *
     * @return el peso en kg
     */
    public Double getValue() {
        return value;
    }

    /**
     * Obtiene el peso como String con unidad
     *
     * @return el peso formateado (ej: "70.0 kg")
     */
    public String asString() {
        return String.format("%.1f kg", value);
    }

    /**
     * Verifica si el peso es válido para un recién nacido (2.5-4.5 kg)
     *
     * @return true si está en el rango de recién nacido
     */
    public boolean isNewbornRange() {
        return value >= 2.5 && value <= 4.5;
    }


    /**
     * Verifica si el peso es válido para un niño de 1 año (8-12 kg)
     *
     * @return true si está en el rango de 1 año
     */
    public boolean isOneYearOldRange() {
        return value >= 8.0 && value <= 12.0;
    }


    /**
     * Verifica si el peso es válido para un niño de 5 años (15-25 kg)
     *
     * @return true si está en el rango de 5 años
     */
    public boolean isFiveYearOldRange() {
        return value >= 15.0 && value <= 25.0;
    }

    /**
     * Verifica si el peso es válido para un niño de 10 años (25-40 kg)
     *
     * @return true si está en el rango de 10 años
     */
    public boolean isTenYearOldRange() {
        return value >= 25.0 && value <= 40.0;
    }

    /**
     * Verifica si el peso es válido para un adolescente (40-70 kg)
     *
     * @return true si está en el rango adolescente
     */
    public boolean isAdolescentRange() {
        return value >= 40.0 && value <= 70.0;
    }


    /**
     * Verifica si el peso está en el rango normal para niños (0.5-40 kg)
     *
     * @return true si está en el rango infantil
     */
    public boolean isChildRange() {
        return value >= 0.5 && value <= 40.0;
    }


    /**
     * Obtiene la categoría de peso según el rango
     *
     * @return la categoría de peso
     */
    public WeightCategory getCategory() {
        if (isNewbornRange()) return WeightCategory.NEWBORN;
        if (isOneYearOldRange()) return WeightCategory.ONE_YEAR_OLD;
        if (isFiveYearOldRange()) return WeightCategory.FIVE_YEAR_OLD;
        if (isTenYearOldRange()) return WeightCategory.TEN_YEAR_OLD;
        if (isAdolescentRange()) return WeightCategory.ADOLESCENT;
        return WeightCategory.OTHER;
    }

    /**
     * Factory method para crear desde String
     *
     * @param weightStr el peso como String (ej: "70.5")
     * @return una instancia de Weight
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static Weight fromString(String weightStr) {
        if (weightStr == null || weightStr.isBlank()) {
            throw new IllegalArgumentException("Weight is required");
        }
        try {
            double weight = Double.parseDouble(weightStr);
            return new Weight(weight);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid weight format. Expected a number");
        }
    }

    /**
     * Verifica si el peso es bajo (menos de 40 kg)
     *
     * @return true si es bajo, false en caso contrario
     */
    public boolean isUnderweight() {
        return value < 40.0;
    }

    /**
     * Verifica si el peso es alto (más de 150 kg)
     *
     * @return true si es alto, false en caso contrario
     */
    public boolean isOverweight() {
        return value > 150.0;
    }

    /**
     * Calcula el IMC (Índice de Masa Corporal) con una altura dada
     *
     * @param heightInMeters la altura en metros
     * @return el IMC calculado
     * @throws IllegalArgumentException si la altura es nula o inválida
     */
    public double calculateBMI(double heightInMeters) {
        if (heightInMeters <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero");
        }
        return value / (heightInMeters * heightInMeters);
    }


    @Override
    public String toString() {
        return asString();
    }




}

