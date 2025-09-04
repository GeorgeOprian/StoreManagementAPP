package com.georgeoprian.storemanagementapp.controller.security;

import com.georgeoprian.storemanagementapp.dtos.security.UserDto;
import com.georgeoprian.storemanagementapp.service.security.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public record CreateUserRequest(@NotBlank String username, @NotBlank String password, Set<String> roles) {}

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(userService.createUser(req.username(), req.password(), req.roles()));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
