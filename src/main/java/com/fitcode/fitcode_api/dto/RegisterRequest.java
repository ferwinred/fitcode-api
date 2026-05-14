package com.fitcode.fitcode_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String displayName;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String dateOfBirth; // formato ISO: "YYYY-MM-DD"

    @NotBlank(message = "El genero es obligatorio")
    private String gender;

    @Positive(message = "La altura debe ser un valor positivo")
    private Integer heightCm;

    @Positive(message = "El peso debe ser un valor positivo")
    private Double weightKg;

    private String metadata;

    @NotBlank(message = "El rol es obligatorio")
    private String role;

}
