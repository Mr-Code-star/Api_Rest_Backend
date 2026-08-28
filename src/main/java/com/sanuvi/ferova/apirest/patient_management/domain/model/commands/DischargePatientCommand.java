package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

public record DischargePatientCommand(
        String patientId,
        String nurseId
) {
}
