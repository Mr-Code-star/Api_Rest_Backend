package com.sanuvi.ferova.apirest.iam.application.internal.queryservices;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetActiveRolesQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetAllRolesQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetRoleByIdQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetRoleByNameQuery;
import com.sanuvi.ferova.apirest.iam.domain.model.valueobjects.Roles;
import com.sanuvi.ferova.apirest.iam.domain.services.RoleQueryService;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<Role> handle(GetRoleByIdQuery query) {
        return roleRepository.findById(query.id());
    }

    @Override
    @Transactional
    public Optional<Role> handle(GetRoleByNameQuery query) {
        var rolesEnum = Roles.valueOf(query.name().toUpperCase());
        return roleRepository.findByName(rolesEnum);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> handle(GetActiveRolesQuery query) {
        return roleRepository.findByIsActiveTrue();
    }
}
