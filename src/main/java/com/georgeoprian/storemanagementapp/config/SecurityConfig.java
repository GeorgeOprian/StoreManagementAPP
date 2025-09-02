package com.georgeoprian.storemanagementapp.config;

import com.georgeoprian.storemanagementapp.model.security.RoleEntity;
import com.georgeoprian.storemanagementapp.model.security.UserEntity;
import com.georgeoprian.storemanagementapp.repository.security.RoleRepository;
import com.georgeoprian.storemanagementapp.repository.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Set;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, RestAuthHandlers handlers) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults()) // Basic Auth
                .authorizeHttpRequests(auth -> auth
                        // public endpoints
//                        .requestMatchers("/api/public/**").permitAll()

                        // Products
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // Users & Roles management
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")

                        // anything else
                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(handlers)   // intercept 401
                        .accessDeniedHandler(handlers)        // intercept 403
                );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt
    }

    @Bean
    CommandLineRunner initData(RoleRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder encoder) {
        return args -> {

            RoleEntity adminRole = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(RoleEntity.builder().name("ROLE_ADMIN").build()));

            RoleEntity userRole = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(RoleEntity.builder().name("ROLE_USER").build()));

            if (userRepo.findByUsername("admin").isEmpty()) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(encoder.encode("admin123")) // BCrypt
                        .enabled(true)
                        .roles(Set.of(adminRole, userRole))
                        .build();
                userRepo.save(admin);
            }

            if (userRepo.findByUsername("user").isEmpty()) {
                UserEntity user = UserEntity.builder()
                        .username("user")
                        .password(encoder.encode("user123")) // BCrypt
                        .enabled(true)
                        .roles(Set.of(userRole))
                        .build();
                userRepo.save(user);
            }
        };
    }
}
