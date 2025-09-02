package com.georgeoprian.storemanagementapp.service.security;

import com.georgeoprian.storemanagementapp.dtos.security.RoleDto;
import com.georgeoprian.storemanagementapp.mappers.security.RoleMapper;
import com.georgeoprian.storemanagementapp.model.security.RoleEntity;
import com.georgeoprian.storemanagementapp.repository.security.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper mapper;

    public RoleDto createRole(String name) {
        if (!name.startsWith("ROLE_")) {
            name = "ROLE_" + name;
        }
        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role already exists");
        }
        RoleEntity saved = roleRepository.save(RoleEntity.builder().name(name).build());
        return mapper.toDto(saved);
    }
}
