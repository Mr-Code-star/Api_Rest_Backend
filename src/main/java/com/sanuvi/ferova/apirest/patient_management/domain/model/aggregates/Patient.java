package com.sanuvi.ferova.apirest.patient_management.domain.model.aggregates;

import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.Gender;
import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.PatientStatus;
import com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects.BirthDate;
import com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects.Height;
import com.sanuvi.ferova.apirest.patient_management.domain.model.valueobjects.Weight;
import com.sanuvi.ferova.apirest.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Aggregate Root
 * <p>
 *     Representa a un paciente en el sistema.
 *     Un paciente puede ser una madre o un niño asociado a una madre.
 * </p>
 *
 * @version 1.0
 */
@Getter
@Document("patients")
public class Patient extends AuditableAbstractAggregateRoot<Patient> {
    @NotBlank
    @Size(max = 50)
    private  String name;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @NotNull
    private BirthDate birthDate;

    @NotNull
    private Weight currentWeight;

    @NotNull
    private Height currentHeight;

    @NotBlank
    private String motherId;

    private String nurseId;

    @NotNull
    private Gender gender;

    private String facilityId;

    @NotNull
    private PatientStatus status;

    /**
     * Constructor para crear un nuevo paciente
     */
    public Patient(String name, String lastName, BirthDate birthDate,
                   Weight currentWeight, Height currentHeight,
                   String motherId, String nurseId, Gender gender,
                   String facilityId) {

        validateName(name);
        validateLastName(lastName);
        ensureMotherExists(motherId);

        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.currentWeight = currentWeight;
        this.currentHeight = currentHeight;
        this.motherId = motherId;
        this.nurseId = nurseId;
        this.gender = gender;
        this.facilityId = facilityId;
        this.status = PatientStatus.ACTIVE;

    }


    // ====== VALIDACIONES PRIVADAS =====

    private void validateName(String name){
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Name is required");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Lastname is required");
        }
    }

    private void ensureMotherExists(String motherId) {
        if (motherId == null || motherId.isBlank()) {
            throw new IllegalArgumentException("Mother is required");
        }
    }

    // ===== MÉTODOS DE NEGOCIO =====

    /**
     * Da de alta al paciente
     * <p>
     *     Solo la enfermera asignada puede dar de alta al paciente.
     *     Al dar de alta, se desasigna la enfermera y el centro de salud.
     * </p>
     *
     * @param nurseId ID de la enfermera que solicita el alta
     * @throws IllegalArgumentException si la enfermera no es la asignada
     */
    public void discharge(String nurseId) {
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }

        if (!nurseId.equals(this.nurseId)) {
            throw new IllegalArgumentException("Only assigned nurse can discharge patient");
        }

        this.status = PatientStatus.DISCHARGED;
        this.nurseId = null;
        this.facilityId = null;
    }

    /**
     * Asigna una enfermera al paciente (agregar a la cartera)
     * <p>
     *     Si el paciente ya tiene enfermera y NO está dado de alta, lanza error.
     *     Si el paciente está dado de alta, permite reasignación y lo reactiva.
     * </p>
     *
     * @param nurseId ID de la enfermera a asignar
     * @param facilityId ID del centro de salud
     * @throws IllegalArgumentException si el paciente ya tiene enfermera asignada
     */
    public void assignNurse(String nurseId, String facilityId) {
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }
        if (facilityId == null || facilityId.isBlank()) {
            throw new IllegalArgumentException("Facility ID is required");
        }

        // Si el paciente ya tiene enfermera y NO está dado de alta, lanzar error
        if (this.nurseId != null && !this.nurseId.isBlank() &&
                this.status != PatientStatus.DISCHARGED) {
            throw new IllegalArgumentException("Patient already has an assigned nurse");
        }

        // Si el paciente está dado de alta, permitir reasignación
        this.nurseId = nurseId;
        this.facilityId = facilityId;
        // Reactivar el paciente automáticamente al reasignarlo
        this.status = PatientStatus.ACTIVE;
    }

    /**
     * Remueve al paciente de la cartera de la enfermera
     * <p>
     *     Una enfermera puede remover un paciente de su cartera cuando:
     *     - Ya no está a su cargo
     *     - El paciente ha sido transferido a otra enfermera
     *     - El paciente ya no necesita seguimiento
     *     - La enfermera ya no está disponible
     * </p>
     *
     * <p>
     *     Al remover de la cartera:
     *     - Se desasigna la enfermera del paciente
     *     - Se elimina el centro de salud
     *     - El paciente queda sin enfermera asignada
     *     - El estado del paciente NO cambia (se mantiene ACTIVE o INACTIVE)
     *     - El paciente puede ser reasignado a otra enfermera posteriormente
     * </p>
     *
     * @param nurseId ID de la enfermera que solicita remover al paciente
     * @throws IllegalArgumentException si la enfermera no es la asignada
     * @throws IllegalArgumentException si el paciente no tiene enfermera asignada
     * @throws IllegalArgumentException si el paciente está dado de alta
     */
    public void removeFromCaseload(String nurseId) {
        // Validar que la enfermera existe
        if (nurseId == null || nurseId.isBlank()) {
            throw new IllegalArgumentException("Nurse ID is required");
        }

        // Validar que el paciente tiene una enfermera asignada
        if (this.nurseId == null || this.nurseId.isBlank()) {
            throw new IllegalArgumentException("Patient does not have an assigned nurse");
        }

        // Validar que la enfermera que solicita la remoción es la asignada
        if (!nurseId.equals(this.nurseId)) {
            throw new IllegalArgumentException("Only assigned nurse can remove patient from caseload");
        }

        // Validar que el paciente no esté dado de alta
        if (this.status == PatientStatus.DISCHARGED) {
            throw new IllegalArgumentException("Cannot remove discharged patient from caseload");
        }

        // Remover al paciente de la cartera de la enfermera
        this.nurseId = null;
        this.facilityId = null;

        // El paciente queda sin enfermera asignada pero sigue activo
        // Puede ser reasignado a otra enfermera posteriormente
    }

    /**
     * Actualiza el peso del paciente
     */
    public void updateWeight(Weight newWeight) {
        if (newWeight == null) {
            throw new IllegalArgumentException("Weight is required");
        }
        this.currentWeight = newWeight;
    }

    /**
     * Actualiza la altura del paciente
     */
    public void updateHeight(Height newHeight) {
        if (newHeight == null) {
            throw new IllegalArgumentException("Height is required");
        }
        this.currentHeight = newHeight;
    }

    /**
     * Actualiza el estado del paciente
     */
    public void updateStatus(PatientStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status is required");
        }
        this.status = newStatus;
    }

    // ===== MÉTODOS DE CONSULTA =====
    /**
     * Verifica si el paciente es menor de edad
     */
    public boolean isMinor() {
        return birthDate.isMinor();
    }

    /**
     * Verifica si el paciente está activo
     */
    public boolean isActive() {
        return this.status == PatientStatus.ACTIVE;
    }

    /**
     * Verifica si el paciente está dado de alta
     */
    public boolean isDischarged() {
        return this.status == PatientStatus.DISCHARGED;
    }

    /**
     * Verifica si el paciente tiene una enfermera asignada
     */
    public boolean hasNurseAssigned() {
        return this.nurseId != null && !this.nurseId.isBlank();
    }

    /**
     * Obtiene el nombre completo del paciente
     */
    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    /**
     * Obtiene la edad del paciente
     */
    public int getAge() {
        return birthDate.calculateAge();
    }

}
