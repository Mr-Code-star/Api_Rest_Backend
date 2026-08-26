package com.sanuvi.ferova.apirest.shared.infrastructure.persistence.mongodb;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    /**
     * Genera el siguiente ID secuencial para una secuencia dada.
     * <p>
     * El método:
     * <p>
     * Busca el documento de secuencia por su nombre
     * Incrementa el valor de 'seq' en 1
     * Si no existe, lo crea con valor inicial 1 (upsert)
     * Retorna el nuevo valor de la secuencia
     * <p>
     * <p>
     * Nota: La operación es atómica gracias a findAndModify de MongoDB.
     *
     * @param seqName Nombre de la secuencia (ej: "product_sequence")
     * @return Nuevo valor de la secuencia (ID generado)
     * @throws NullPointerException si la secuencia no puede ser creada
     */
    public String generateSequence(String seqName) {
        var query = new Query(Criteria.where("_id").is(seqName));
        var update = new Update().inc("seq", 1);
        var options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        var sequence = mongoOperations.findAndModify(query, update, options, DatabaseSequence.class);
        return Objects.requireNonNull(sequence).getSeq();
    }
}
