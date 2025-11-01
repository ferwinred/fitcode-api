package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.*;
import com.fitcode.fitcode_api.services.AuthService;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController() {
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            AuthResponse created = authService.register(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception ex) {
            logger.error("Error en registro: " + ex.getMessage());

            if (ex.getClass().getName().equals("java.lang.IllegalStateException")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "bad_request",
                                "message", ex.getMessage()));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            System.out.println("Login attempt for: " + req.getEmail() + " " + req.getPassword());
            AuthResponse token = authService.login(req);
            return ResponseEntity.ok(token);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            // BadCredentialsException y otras -> 401
            logger.warn("AuthenticationException for {} : {}", req.getEmail(), ex.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "unauthorized",
                            "message", "El correo o la contraseña son incorrectos"));
        } catch (org.springframework.web.server.ResponseStatusException rse) {
            // si ya lanzas ResponseStatusException en el service, repropagamos
            throw rse;
        } catch (Exception ex) {
            logger.error("Error en login", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno"));
        }
    }
}
