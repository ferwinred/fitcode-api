package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.AuthRequest;
import com.fitcode.fitcode_api.dto.AuthResponse;
import com.fitcode.fitcode_api.dto.RegisterRequest;
import com.fitcode.fitcode_api.models.Role;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.repository.RoleRepository;
import com.fitcode.fitcode_api.repository.UserRepository;
import com.fitcode.fitcode_api.security.JwtUtil;
import com.fitcode.fitcode_api.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(AuthRequest req) {
        // autentica con Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        UserDetails ud = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(ud);

        List<String> roles = ud.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        return new AuthResponse(token, ud.getUsername(), roles);
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("El email ya está en uso");
        }

        // asignar rol por defecto, por ejemplo ROLE_USER
        Role role = roleRepository.findByName(req.getRole())
                .orElseGet(() -> roleRepository
                        .save(Role.builder().name(req.getRole()).description("Usuario por defecto").build()));

        System.out.println("User to Create: " + req.getEmail() + " " + req.getFullName() + " " + req.getPassword() + " "
                + req.getRole() + " " + req.getDisplayName());

        User u = User.builder()
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .fullName(req.getFullName()) // puedes requerir más campos en request de registro
                .displayName(req.getDisplayName())
                .dateOfBirth(LocalDate.parse(req.getDateOfBirth())) // temporal: solicitar fecha real en registro
                .sex(req.getGender())
                .heightCm(req.getHeightCm())
                .weightKg(req.getWeightKg())
                .metadata(req.getMetadata())
                .role(role)
                .build();

        userRepository.save(u);

        UserDetails ud = CustomUserDetails.fromUser(u);
        String token = jwtUtil.generateToken(ud);

        List<String> roles = ud.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());

        return new AuthResponse(token, ud.getUsername(), roles);
    }
}
