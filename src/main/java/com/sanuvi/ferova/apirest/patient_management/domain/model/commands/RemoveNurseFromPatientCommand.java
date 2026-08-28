package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

public record RemoveNurseFromPatientCommand(
        String patientId,
        String nurseId
) {
    public RemoveNurseFromPatientCommand {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
    }
}
