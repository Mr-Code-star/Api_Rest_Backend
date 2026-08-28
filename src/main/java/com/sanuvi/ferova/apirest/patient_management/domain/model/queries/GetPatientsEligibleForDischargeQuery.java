package com.sanuvi.ferova.apirest.patient_management.domain.model.queries;


/**
 * Consulta para obtener pacientes elegibles para alta
 * <p>
 *     Retorna todos los pacientes que cumplen con los criterios para ser dados de alta:
 *     - Pacientes con estado ACTIVE
 *     - Pacientes que tienen enfermera asignada
 *     - Pacientes que cumplen con los criterios clínicos para alta
 * </p>
 *
 * @param nurseId ID de la enfermera (opcional, para filtrar por enfermera específica)
 * @param searchTerm término de búsqueda (opcional, para filtrar por nombre, apellido o DNI)
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record GetPatientsEligibleForDischargeQuery(
        String nurseId,
        String searchTerm
) {
    /**
     * Constructor para obtener todos los pacientes elegibles para alta (sin filtros)
     */
    public GetPatientsEligibleForDischargeQuery() {
        this(null, null);
    }

    /**
     * Constructor para obtener pacientes elegibles para alta de una enfermera específica
     *
     * @param nurseId ID de la enfermera
     */
    public GetPatientsEligibleForDischargeQuery(String nurseId) {
        this(nurseId, null);
    }

    /**
     * Constructor para obtener pacientes elegibles para alta con término de búsqueda
     *
     * @param searchTerm término de búsqueda
     */
    public GetPatientsEligibleForDischargeQuery(String nurseId, String searchTerm) {
        this.nurseId = nurseId;
        this.searchTerm = searchTerm;
    }

    /**
     * Verifica si el filtro por enfermera está activo
     *
     * @return true si se filtrará por enfermera, false en caso contrario
     */
    public boolean hasNurseFilter() {
        return nurseId != null && !nurseId.isBlank();
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