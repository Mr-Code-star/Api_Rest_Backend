package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;

/**
 * Consulta para obtener información básica de un paciente
 * <p>
 *     Retorna solo la información esencial del paciente:
 *     - ID
 *     - Nombre completo
 *     - Edad
 *     - Género
 *     - Estado
 *     - ID de la madre
 *     - ID de la enfermera asignada
 * </p>
 *
 * @param patientId ID del paciente
 *
 * @version 1.0
 */
public record GetPatientBasicInfoQuery(String patientId) {
    public GetPatientBasicInfoQuery {
        if (patientId == null || patientId.isBlank()) {
            throw new IllegalArgumentException("Patient ID is required");
        }
    }
}
