package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

public record AssignNurseToPatientCommand(
    String patientId,
    String nurseId,
    String facilityId

) {
    public AssignNurseToPatientCommand {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
        if (facilityId == null || facilityId.isBlank()) {
            throw new IllegalArgumentException("Facility ID is required");
        }
    }
}
