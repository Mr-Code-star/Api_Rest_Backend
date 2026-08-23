package com.sanuvi.ferova.apirest.iam.application.internal.commandservices;

import com.sanuvi.ferova.apirest.iam.domain.model.commands.SeedRolesCommand;
import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Roles;
import com.sanuvi.ferova.apirest.iam.domain.services.RoleCommandService;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.RoleRepository;
import com.sanuvi.ferova.apirest.shared.infrastructure.persistence.mongodb.SequenceGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;
    private final SequenceGeneratorService sequenceGeneratorService;


    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                var roleEntity = new Role(Roles.valueOf(role.name()));
                roleEntity.setId(sequenceGeneratorService.generateSequence("roles_sequence"));
                roleRepository.save(roleEntity);
            }
        });
    }
}
