package com.fitcode.fitcode_api.config;

import com.fitcode.fitcode_api.models.Role;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.repository.RoleRepository;
import com.fitcode.fitcode_api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class SeedDataRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataRunner.class);

    @Value("${app.seed.admin.username:admin}")
    private String adminUsername;

    @Value("${app.seed.admin.email:admin@local}")
    private String adminEmail;

    @Value("${app.seed.admin.password:admin123}")
    private String adminPassword;

    @Bean
    public CommandLineRunner seedDatabase(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // 1) Crear roles por defecto
            createRoleIfNotExists(roleRepository, "admin", "Administrador del sistema");
            createRoleIfNotExists(roleRepository, "user", "Usuario estándar");
            createRoleIfNotExists(roleRepository, "coach", "Entrenador / creador de rutinas");

            // 2) Crear usuario admin si no existe
            Optional<User> adminByEmail = userRepository.findByEmail(adminEmail);

            if (adminByEmail.isPresent()) {
                log.info("Admin user already exists (email or username found). Skipping admin creation.");
            } else {
                Role adminRole = roleRepository.findByName("admin")
                        .orElseThrow(() -> new IllegalStateException("Role 'admin' should have been created"));

                User admin = User.builder()
                        .email(adminEmail)
                        .fullName("Dani Perez")
                        .passwordHash(passwordEncoder.encode(adminPassword))
                        .displayName("Administrador")
                        .role(adminRole)
                        .sex("F")
                        .heightCm(170)
                        .weightKg(70.5)
                        .metadata(
                                "{ \"preferencias\": [ \"ganar masa\", \"cardio\", \"fortalecimiento\" ], \"objetivos\": { \"peso_objetivo_kg\": 65.0, \"fecha_objetivo\": \"2024-12-31\" }, \"nivel\": \"intermedio\" }")
                        .dateOfBirth(Date.valueOf("2000-05-05").toLocalDate())
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(admin);
                log.info("Admin user created -> email: {}", adminEmail);
            }
        };
    }

    private void createRoleIfNotExists(RoleRepository roleRepository, String roleName, String description) {
        roleRepository.findByName(roleName).ifPresentOrElse(
                r -> log.debug("Role '{}' already exists (id={}).", roleName, r.getId()),
                () -> {
                    Role role = Role.builder()
                            .name(roleName)
                            .description(description)
                            .createdAt(LocalDateTime.now())
                            .build();
                    roleRepository.save(role);
                    log.info("Role '{}' created.", roleName);
                });
    }
}
