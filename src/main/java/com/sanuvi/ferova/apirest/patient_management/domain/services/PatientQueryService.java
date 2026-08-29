package com.sanuvi.ferova.apirest.patient_management.domain.services;

import com.sanuvi.ferova.apirest.patient_management.domain.model.aggregates.Patient;
import com.sanuvi.ferova.apirest.patient_management.domain.model.queries.GetPatientByIdQuery;
import com.sanuvi.ferova.apirest.patient_management.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface PatientQueryService {
    /**
     * Obtiene el conteo de pacientes activos
     *
     * @param query la consulta {@link GetActivePatientsCountQuery}
     * @return el número de pacientes activos
     */
    long handle(GetActivePatientsCountQuery query);

    /**
     * Obtiene la información básica de un paciente
     *
     * @param query la consulta {@link GetPatientBasicInfoQuery}
     * @return un {@link Optional} con la información básica del paciente
     */
    Optional<Patient> handle(GetPatientBasicInfoQuery query);

    /**
     * Obtiene todos los pacientes de una madre
     *
     * @param query la consulta {@link GetPatientsByMotherIdQuery}
     * @return una lista con los pacientes de la madre
     */
    List<Patient> handle(GetPatientsByMotherIdQuery query);

    /**
     * Obtiene todos los pacientes de una enfermera (cartera)
     *
     * @param query la consulta {@link GetPatientsByNurseIdQuery}
     * @return una lista con los pacientes de la enfermera
     */
    List<Patient> handle(GetPatientsByNurseIdQuery query);

    /**
     * Obtiene los pacientes elegibles para alta
     *
     * @param query la consulta {@link GetPatientsEligibleForDischargeQuery}
     * @return una lista con los pacientes elegibles para alta
     */
    List<Patient> handle(GetPatientsEligibleForDischargeQuery query);

    /**
     * Obtiene un paciente por su ID
     *
     * @param query la consulta {@link GetPatientByIdQuery}
     * @return un {@link Optional} que contiene el paciente, o vacío si no existe
     */
    Optional<Patient> handle(GetPatientByIdQuery query);

    /**
     * Obtiene todos los pacientes asignados a una enfermera
     *
     * @param query la consulta {@link GetPatientsAssignedToNurseQuery}
     * @return una lista con los pacientes asignados a la enfermera
     */
    List<Patient> handle(GetPatientsAssignedToNurseQuery query);
}
