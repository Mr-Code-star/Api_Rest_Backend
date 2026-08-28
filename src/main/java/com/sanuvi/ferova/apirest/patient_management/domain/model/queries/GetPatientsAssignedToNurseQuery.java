package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;

/**
 * Consulta para obtener todos los pacientes asignados a una enfermera
 * <p>
 *     Retorna todos los pacientes que están activos y asignados a una enfermera específica.
 *     Similar a {@link GetPatientsByNurseIdQuery} pero solo incluye pacientes activos.
 * </p>
 *
 * @param nurseId ID de la enfermera
 * @param includeDischarged indica si se deben incluir pacientes dados de alta
 * @param searchTerm término de búsqueda (opcional, para filtrar por nombre, apellido o DNI)
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record GetPatientsAssignedToNurseQuery(
        String nurseId,
        boolean includeDischarged,
        String searchTerm
) {
    /**
     * Constructor para obtener solo pacientes activos (sin incluir dados de alta)
     *
     * @param nurseId ID de la enfermera
     */
    public GetPatientsAssignedToNurseQuery(String nurseId) {
        this(nurseId, false, null);
    }

    /**
     * Constructor para obtener pacientes activos con término de búsqueda
     *
     * @param nurseId ID de la enfermera
     * @param searchTerm término de búsqueda
     */
    public GetPatientsAssignedToNurseQuery(String nurseId, String searchTerm) {
        this(nurseId, false, searchTerm);
    }

    /**
     * Constructor para incluir o excluir pacientes dados de alta
     *
     * @param nurseId ID de la enfermera
     * @param includeDischarged indica si se deben incluir pacientes dados de alta
     */
    public GetPatientsAssignedToNurseQuery(String nurseId, boolean includeDischarged) {
        this(nurseId, includeDischarged, null);
    }

    public GetPatientsAssignedToNurseQuery {
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
    }

    /**
     * Verifica si el término de búsqueda está presente
     *
     * @return true si hay término de búsqueda, false en caso contrario
     */
    public boolean hasSearchTerm() {
        return searchTerm != null && !searchTerm.isBlank();
    }
}