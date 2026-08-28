package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;

/**
 * Consulta para obtener el conteo de pacientes activos
 * <p>
 *     Retorna el número total de pacientes con estado ACTIVE.
 *     Útil para dashboards y estadísticas.
 * </p>
 *
 * @version 1.0
 */
public record GetActivePatientsCountQuery(String nurseId) {
    public GetActivePatientsCountQuery {
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
    }
}
