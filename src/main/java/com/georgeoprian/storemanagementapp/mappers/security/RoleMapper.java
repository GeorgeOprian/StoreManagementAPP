package com.georgeoprian.storemanagementapp.mappers.security;

import com.georgeoprian.storemanagementapp.dtos.security.RoleDto;
import com.georgeoprian.storemanagementapp.model.security.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(RoleEntity entity);
    RoleEntity toEntity(RoleDto dto);
}
