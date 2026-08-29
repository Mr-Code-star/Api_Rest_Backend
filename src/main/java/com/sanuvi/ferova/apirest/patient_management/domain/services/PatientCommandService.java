package com.sanuvi.ferova.apirest.patient_management.domain.services;

import com.sanuvi.ferova.apirest.patient_management.domain.model.aggregates.Patient;
import com.sanuvi.ferova.apirest.patient_management.domain.model.commands.*;

import java.util.Optional;

/**
 * Servicio de comandos para la gestión de pacientes
 * <p>
 *     Esta interfaz define las operaciones de escritura para la gestión de pacientes.
 * </p>
 *
 * @version 1.0
 */
public interface PatientCommandService {
    /**
     * Registra un nuevo paciente
     *
     * @param command el comando {@link RegisterPatientCommand} con los datos del paciente
     * @return un {@link Optional} que contiene el paciente creado, o vacío si falla
     */
    Optional<Patient> handle(RegisterPatientCommand command);

    /**
     * Da de alta a un paciente
     *
     * @param command el comando {@link DischargePatientCommand}
     * @return un {@link Optional} que indica éxito (conteniendo null) o fracaso (vacío)
     */
    Optional<Void> handle(DischargePatientCommand command);

    /**
     * Actualiza el peso de un paciente
     *
     * @param command el comando {@link UpdatePatientWeightCommand}
     * @return un {@link Optional} que contiene el paciente actualizado, o vacío si falla
     */
    Optional<Patient> handle(UpdatePatientWeightCommand command);

    /**
     * Actualiza la altura de un paciente
     *
     * @param command el comando {@link UpdatePatientHeightCommand}
     * @return un {@link Optional} que contiene el paciente actualizado, o vacío si falla
     */
    Optional<Patient> handle(UpdatePatientHeightCommand command);

    /**
     * Actualiza el estado de un paciente
     *
     * @param command el comando {@link UpdatePatientStatusCommand}
     * @return un {@link Optional} que contiene el paciente actualizado, o vacío si falla
     */
    Optional<Patient> handle(UpdatePatientStatusCommand command);

    /**
     * Remueve una enfermera de un paciente (quitar de cartera)
     *
     * @param command el comando {@link RemoveNurseFromPatientCommand}
     * @return un {@link Optional} que contiene el paciente actualizado, o vacío si falla
     */
    Optional<Patient> handle(RemoveNurseFromPatientCommand command);

    /**
     * Asigna una enfermera a un paciente
     *
     * @param command el comando {@link AssignNurseToPatientCommand}
     * @return un {@link Optional} que contiene el paciente actualizado, o vacío si falla
     */
    Optional<Patient> handle(AssignNurseToPatientCommand command);
}
