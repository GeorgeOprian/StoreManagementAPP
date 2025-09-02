package com.georgeoprian.storemanagementapp.controller.security;

import com.georgeoprian.storemanagementapp.dtos.security.RoleDto;
import com.georgeoprian.storemanagementapp.service.security.RoleService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    public record CreateRoleRequest(@NotBlank String name) {}

    @PostMapping
    public ResponseEntity<RoleDto> create(@RequestBody CreateRoleRequest req) {
        return ResponseEntity.ok(roleService.createRole(req.name()));
    }
}
