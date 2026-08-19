package com.sanuvi.ferova.apirest.shared.domain.model.aggregates;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;

/**
 * Es una clase base para agregados que requieren auditoría.
 * Hereda de AbstractAggregateRoot (de Spring)
 * y usa anotaciones de auditoría propias de MongoDB
 * (aunque usa @CreatedDate y @LastModifiedDate de Spring Data común).
 * El tipo genérico T representa el propio aggregate root concreto.
 * @param <T>
 */
@Getter
public class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>> extends AbstractAggregateRoot<T> {
    @Id
    private Long id;

    @CreatedDate
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updateAt;

    /**
     * Registra un evento de dominio en el aggregate root.
     * @param event
     */
    public void addDomainEvent(Object event) {
        registerEvent(event);
    }
}
