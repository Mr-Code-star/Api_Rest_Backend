package com.sanuvi.ferova.apirest.iam.domain.model.entities;

import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad de rol para MongoDB
 * <p>
 *     Esta entidad representa el rol de un usuario en el sistema.
 * </p>
 */
@Document(collection = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
public class Role {

    @Id
    private Long id;

    @Indexed(unique = true)
    private Roles name;

    private String displayName;

    private String description;

    private Integer level;

    private Set<String> permissions = new HashSet<>();

    private Boolean isActive = true;

    /**
     * Constructor para crear un rol desde un enum Roles
     *
     * @param name el enum Roles
     */
    public Role(Roles name) {
        this.name = name;
        this.displayName = getDefaultDisplayName(name);
        this.description = getDefaultDescription(name);
        this.level = getDefaultLevel(name);
        this.isActive = true;
        this.permissions = new HashSet<>();
    }

    /**
     * Constructor para crear un rol desde un enum Roles con permisos
     *
     * @param name el enum Roles
     * @param permissions los permisos del rol
     */
    public Role(Roles name, Set<String> permissions) {
        this(name);
        this.permissions = permissions != null ? permissions : new HashSet<>();
    }

    /**
     * Obtiene el nombre del rol como string
     *
     * @return el nombre del rol como string
     */
    public String getStringName() {
        return name != null ? name.name() : null;
    }

    /**
     * Obtiene el nombre de visualización por defecto para cada rol
     */
    private String getDefaultDisplayName(Roles name) {
        return switch (name) {
            case ADMIN -> "Administrador";
            case NURSE -> "Enfermera";
            case MOTHER -> "Madre";
        };
    }

    /**
     * Obtiene la descripción por defecto para cada rol
     */
    private String getDefaultDescription(Roles name) {
        return switch (name) {
            case ADMIN -> "Administrador con acceso completo al sistema";
            case NURSE -> "Personal médico con acceso a pacientes y registros médicos";
            case MOTHER -> "Usuario madre con acceso a su perfil e hijos";
        };
    }

    /**
     * Obtiene el nivel por defecto para cada rol
     */
    private Integer getDefaultLevel(Roles name) {
        return switch (name) {
            case ADMIN -> 3;
            case NURSE -> 2;
            case MOTHER -> 1;
        };
    }

    /**
     * Obtiene el rol a partir de su nombre
     *
     * @param name el nombre del rol
     * @return el rol
     * @throws IllegalArgumentException si el nombre no corresponde a ningún rol
     */
    public static Role fromName(String name) {
        Roles rolesEnum = Roles.valueOf(name.toUpperCase());
        return new Role(rolesEnum);
    }
}
