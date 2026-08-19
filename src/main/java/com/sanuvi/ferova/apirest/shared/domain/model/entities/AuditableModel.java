package com.sanuvi.ferova.apirest.shared.domain.model.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * Clase Base Abstracta para entidades que requieren auditoria auotomatica
 * Esta clase NO incluye soporte para eventos de dominio.
 *
 */
public class AuditableModel {
    /**
     * Identificador único de la entidad en la base de datos.
     */
    @Id
    @Getter
    @Setter
    private Long id;

    /**
     * Fecha y hora en que la entidad fue creada por primera vez.
     */
    @Getter
    @CreatedDate
    private Date createdAt;

    /**
     * Fecha y hora de la última modificación de la entidad.
     */
    @Getter
    @LastModifiedDate
    private Date updateAt;
}
