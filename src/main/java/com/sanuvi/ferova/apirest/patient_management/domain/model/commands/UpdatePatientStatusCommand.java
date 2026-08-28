package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.PatientStatus;

/**
 * Comando para actualizar el estado de un paciente
 * <p>
 *     Este comando permite cambiar el estado de un paciente.
 *     Estados posibles: ACTIVE, INACTIVE, DISCHARGED
 * </p>
 *
 * @param patientId ID del paciente
 * @param status nuevo estado del paciente
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record UpdatePatientStatusCommand(
        String patientId,
        PatientStatus status
) {
    public UpdatePatientStatusCommand {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
    }
}