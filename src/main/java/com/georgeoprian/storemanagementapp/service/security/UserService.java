package com.georgeoprian.storemanagementapp.service.security;

import com.georgeoprian.storemanagementapp.dtos.security.UserDto;
import com.georgeoprian.storemanagementapp.mappers.security.UserMapper;
import com.georgeoprian.storemanagementapp.model.security.RoleEntity;
import com.georgeoprian.storemanagementapp.model.security.UserEntity;
import com.georgeoprian.storemanagementapp.repository.security.RoleRepository;
import com.georgeoprian.storemanagementapp.repository.security.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    @Transactional
    public UserDto createUser(@NotBlank String username,
            @NotBlank String rawPassword,
            Set<String> roleNames) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Set<RoleEntity> roles = roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + name)))
                .collect(java.util.stream.Collectors.toSet());

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword)) // BCrypt
                .roles(roles)
                .enabled(true)
                .build();

        return mapper.toDto(userRepository.save(user));
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
