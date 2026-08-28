package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;

/**
 * Consulta para obtener todos los pacientes de una madre
 * <p>
 *     Retorna todos los pacientes (hijos) asociados a una madre específica.
 *     Útil para que una madre vea a todos sus hijos registrados.
 * </p>
 *
 * @param motherId ID de la madre
 *
 * @version 1.0
 */
public record GetPatientsByMotherIdQuery(
        String motherId
) {
    public GetPatientsByMotherIdQuery {
        if (motherId == null || motherId.isBlank()) {
            throw new IllegalArgumentException("Mother ID is required");
        }
    }
}