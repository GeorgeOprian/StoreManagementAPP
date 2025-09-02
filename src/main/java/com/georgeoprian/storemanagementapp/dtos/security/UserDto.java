package com.georgeoprian.storemanagementapp.dtos.security;

import java.util.Set;

public record UserDto(Long id, String username, Set<String> roles, boolean enabled) {}
