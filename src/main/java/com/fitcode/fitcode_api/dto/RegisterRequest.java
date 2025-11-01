package com.fitcode.fitcode_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String displayName;

    private String fullName;

    private String dateOfBirth; // formato ISO: "YYYY-MM-DD"

    private String gender;

    private Integer heightCm;

    private Double weightKg;

    private String metadata;

    private String role;

}