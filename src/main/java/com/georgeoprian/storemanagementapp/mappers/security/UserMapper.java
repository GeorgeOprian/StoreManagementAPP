package com.georgeoprian.storemanagementapp.mappers.security;



import com.georgeoprian.storemanagementapp.dtos.security.UserDto;
import com.georgeoprian.storemanagementapp.model.security.RoleEntity;
import com.georgeoprian.storemanagementapp.model.security.UserEntity;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoles(entity.getRoles()))")
    UserDto toDto(UserEntity entity);

    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserDto dto);

    default Set<String> mapRoles(Set<RoleEntity> roles) {
        return roles.stream().map(RoleEntity::getName).collect(Collectors.toSet());
    }
}
