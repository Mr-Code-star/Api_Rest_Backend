package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

/**
 * Comando para actualizar la altura de un paciente
 * <p>
 *     Este comando permite actualizar la altura actual del paciente.
 *     La altura debe estar en el rango de 20 cm a 250 cm.
 * </p>
 *
 * @param patientId ID del paciente
 * @param height nueva altura en centímetros
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record UpdatePatientHeightCommand(
        String patientId,
        Double height
) {
    public UpdatePatientHeightCommand {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height is required and must be greater than zero");
        }
        if (height < 20.0 || height > 250.0) {
            throw new IllegalArgumentException("Height must be between 20 cm and 250 cm");
        }
    }
}