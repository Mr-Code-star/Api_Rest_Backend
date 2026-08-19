package com.sanuvi.ferova.apirest.shared.infrastructure.persistence.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

/**
 * Configuración de MongoDB para la aplicación.
 * Habilita auditoría automática y configura el mapeo de tipos.
 */
@Configuration
@EnableMongoAuditing  // Habilita @CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy
public class MongoConfig {

    /**
     * Configura el MongoTemplate sin el campo _class en los documentos.
     * Esto evita que MongoDB guarde información de tipo en cada documento.
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory,
                                       MappingMongoConverter converter) {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(databaseFactory, converter);
    }
}
