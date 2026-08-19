package com.sanuvi.ferova.apirest.shared.infrastructure.persistence.mongodb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entidad que almacena los contadores de secuencia para generar IDs autoincrementales en MongoDB.
 *
 * MongoDB no tiene autoincrement por defecto como SQL, esta clase proporciona
 * un mecanismo para simular IDs secuenciales de manera atómica.
 **/
@Document(collection = "database_sequence")
@NoArgsConstructor
@Getter
@Setter
public class DatabaseSequence {

    /**
     * Identificador de la secuencia.
     */
    @Id
    private String id;

    /**
     * Valor actual del contador.
     */
    private long seq;
}
