package com.sanuvi.ferova.apirest.iam.application.internal.queryservices;

import com.sanuvi.ferova.apirest.iam.domain.model.entities.Role;
import com.sanuvi.ferova.apirest.iam.domain.model.queries.GetAllRolesQuery;
import com.sanuvi.ferova.apirest.iam.domain.services.RoleQueryService;
import com.sanuvi.ferova.apirest.iam.infrastructure.persistence.mongodb.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

}
