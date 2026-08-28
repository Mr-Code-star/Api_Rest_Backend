package com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects;

import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.HeightCategory;

/**
 * Value Object para la altura
 * <p>
 *     Representa la altura de una persona en centímetros.
 *     Valida que la altura esté en un rango realista (20 cm - 250 cm).
 * </p>
 *
 * <p>
 *     Rangos de altura por edad:
 *     <ul>
 *         <li>Recién nacido: 45 - 55 cm</li>
 *         <li>1 año: 70 - 80 cm</li>
 *         <li>5 años: 100 - 115 cm</li>
 *         <li>10 años: 130 - 145 cm</li>
 *         <li>Adolescente: 140 - 180 cm</li>
 *         <li>Adulto: 150 - 200 cm</li>
 *     </ul>
 * </p>
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */

public record Height (Double value) {

    /**
     * Altura mínima en centímetros (20 cm - recién nacido prematuro)
     */
    private static final double MIN_HEIGHT = 20.0;

    /**
     * Altura máxima en centímetros (250 cm - máximo realista)
     */
    private static final double MAX_HEIGHT = 250.0;

    /**
     * Constructor compacto con validación
     *
     * @param value la altura en centímetros
     * @throws IllegalArgumentException si la altura es nula, menor a 20 cm o mayor a 250 cm
     */
    public Height {
        if (value == null) {
            throw new IllegalArgumentException("Height is required");
        }
        if (value < MIN_HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Height must be at least %.1f cm (minimum for newborns)", MIN_HEIGHT)
            );
        }
        if (value > MAX_HEIGHT) {
            throw new IllegalArgumentException(
                    String.format("Height must not exceed %.1f cm", MAX_HEIGHT)
            );
        }
    }

    /**
     * Obtiene la altura en centímetros
     *
     * @return la altura en cm
     */
    public Double getValue() {
        return value;
    }

    /**
     * Verifica si la altura es válida para un recién nacido (45-55 cm)
     *
     * @return true si está en el rango de recién nacido
     */
    public boolean isNewbornRange() {
        return value >= 45.0 && value <= 55.0;
    }

    /**
     * Verifica si la altura es válida para un niño de 1 año (70-80 cm)
     *
     * @return true si está en el rango de 1 año
     */
    public boolean isOneYearOldRange() {
        return value >= 70.0 && value <= 80.0;
    }

    /**
     * Verifica si la altura es válida para un niño de 5 años (100-115 cm)
     *
     * @return true si está en el rango de 5 años
     */
    public boolean isFiveYearOldRange() {
        return value >= 100.0 && value <= 115.0;
    }

    /**
     * Verifica si la altura es válida para un niño de 10 años (130-145 cm)
     *
     * @return true si está en el rango de 10 años
     */
    public boolean isTenYearOldRange() {
        return value >= 130.0 && value <= 145.0;
    }

    /**
     * Verifica si la altura es válida para un adolescente (140-180 cm)
     *
     * @return true si está en el rango adolescente
     */
    public boolean isAdolescentRange() {
        return value >= 140.0 && value <= 180.0;
    }

    /**
     * Verifica si la altura está en el rango normal para niños (20-150 cm)
     *
     * @return true si está en el rango infantil
     */
    public boolean isChildRange() {
        return value >= 20.0 && value <= 150.0;
    }

    /**
     * Obtiene la categoría de altura según el rango
     *
     * @return la categoría de altura
     */
    public HeightCategory getCategory() {
        if (isNewbornRange()) return HeightCategory.NEWBORN;
        if (isOneYearOldRange()) return HeightCategory.ONE_YEAR_OLD;
        if (isFiveYearOldRange()) return HeightCategory.FIVE_YEAR_OLD;
        if (isTenYearOldRange()) return HeightCategory.TEN_YEAR_OLD;
        if (isAdolescentRange()) return HeightCategory.ADOLESCENT;
        return HeightCategory.OTHER;
    }

    /**
     * Factory method para crear desde String
     *
     * @param heightStr la altura como String (ej: "170.5")
     * @return una instancia de Height
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static Height fromString(String heightStr) {
        if (heightStr == null || heightStr.isBlank()) {
            throw new IllegalArgumentException("Height is required");
        }
        try {
            double height = Double.parseDouble(heightStr);
            return new Height(height);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid height format. Expected a number");
        }
    }

}
