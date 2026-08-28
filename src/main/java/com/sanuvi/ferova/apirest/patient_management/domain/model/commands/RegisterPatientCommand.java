package com.sanuvi.ferova.apirest.patient_management.domain.model.commands;

import com.sanuvi.ferova.apirest.patient_management.domain.model.enumerations.Gender;

import java.time.LocalDate;

/**
 * Comando para registrar un nuevo paciente
 *
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public record RegisterPatientCommand(
        String name,
        String lastName,
        LocalDate birthDate,
        Double weight,
        Double height,
        String motherId,
        String nurseId,
        Gender gender,
        String facilityId
) {
    public RegisterPatientCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Lastname is required");
        }
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (weight == null || weight <= 0) {
            throw new IllegalArgumentException("Weight is required and must be greater than zero");
        }
        if (height == null || height <= 0) {
            throw new IllegalArgumentException("Height is required and must be greater than zero");
        }
        if (motherId == null || motherId.isBlank()) {
            throw new IllegalArgumentException("Mother ID is required");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender is required");
        }
    }

    /**
     * Builder para crear el comando de forma fluida
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder para RegisterPatientCommand
     */
    public static class Builder {
        private String name;
        private String lastName;
        private LocalDate birthDate;
        private Double weight;
        private Double height;
        private String motherId;
        private String nurseId;
        private Gender gender;
        private String facilityId;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = LocalDate.parse(birthDate);
            return this;
        }

        public Builder weight(Double weight) {
            this.weight = weight;
            return this;
        }

        public Builder height(Double height) {
            this.height = height;
            return this;
        }

        public Builder motherId(String motherId) {
            this.motherId = motherId;
            return this;
        }

        public Builder nurseId(String nurseId) {
            this.nurseId = nurseId;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = Gender.valueOf(gender.toUpperCase());
            return this;
        }

        public Builder facilityId(String facilityId) {
            this.facilityId = facilityId;
            return this;
        }

        public RegisterPatientCommand build() {
            return new RegisterPatientCommand(
                    name, lastName, birthDate, weight, height,
                    motherId, nurseId, gender, facilityId
            );
        }
    }
}