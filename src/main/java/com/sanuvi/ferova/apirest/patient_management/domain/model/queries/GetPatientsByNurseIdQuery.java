package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;

/**
 * Consulta para obtener todos los pacientes de una enfermera (cartera)
 * <p>
 *     Retorna todos los pacientes asignados a una enfermera específica.
 *     Útil para que una enfermera vea su cartera de pacientes.
 * </p>
 *
 * @param nurseId ID de la enfermera
 *
 * @version 1.0
 */
public record GetPatientsByNurseIdQuery(
        String nurseId
) {
    public GetPatientsByNurseIdQuery {
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
    }
}