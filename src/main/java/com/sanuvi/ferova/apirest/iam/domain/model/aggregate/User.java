package com.sanuvi.ferova.apirest.iam.domain.model.aggregate;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Dni;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Email;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Password;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Phone;
import com.sanuvi.ferova.apirest.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "users")
@Getter
@NoArgsConstructor
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String lastName;


    @Setter
    private Password password;

    @DBRef
    private Role role;

    @Indexed(unique = true)  // MongoDB: índice único
    private Dni dni;

    @Indexed(unique = true)  // MongoDB: índice único
    private Email email;

    @Indexed(unique = true)  // MongoDB: índice único
    private Phone phone;

    // ==== CAMPOS PARA EL RESET DE CONTRASEÑA ====

    @Setter
    private String resetCode;

    @Setter
    private LocalDateTime resetCodeExpiry;

    // Constructor para creación (sin ID)
    public User(String name, String lastName, Password password,
                Role role, Dni dni, Email email, Phone phone) {
        validateName(name);
        validateLastName(lastName);

        this.name = name;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.dni = dni;
        this.email = email;
        this.phone = phone;
    }


    // Validaciones Privadas

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
    }

    private void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Lastname is required");
        }
    }

    // Metodos del Negocio

    public void changePassword(Password newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("Password is required");
        }

        this.password = newPassword;
    }

    public void updateEmail(Email newEmail) {
        if (newEmail == null) {
            throw new IllegalArgumentException("Email is required");
        }
        this.email = newEmail;
    }

    public boolean isResetCodeValid (String code) {
        if (this.resetCode == null || this.resetCodeExpiry == null)
            return false;

        return this.resetCode.equals(code) && this.resetCodeExpiry.isAfter(LocalDateTime.now());
    }

    public void clearResetCode() {
        this.resetCode = null;
        this.resetCodeExpiry = null;
    }

    public String getFullName() {
        return this.name + " " + this.lastName;
    }
    public String getRoleName() { return role != null ? role.getStringName() : null;}
}
