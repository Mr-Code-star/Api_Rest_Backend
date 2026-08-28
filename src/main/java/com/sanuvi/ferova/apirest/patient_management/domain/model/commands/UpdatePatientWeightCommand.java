package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

/**
 * Comando para actualizar el peso de un paciente
 * <p>
 *     Este comando permite actualizar el peso actual del paciente.
 *     El peso debe ser mayor a 0.
 * </p>
 *
 * @param patientId ID del paciente
 * @param weight nuevo peso en kilogramos
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record UpdatePatientWeightCommand(
        String patientId,
        Double weight
) {
    public UpdatePatientWeightCommand {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight is required and must be greater than zero");
        }
    }
}