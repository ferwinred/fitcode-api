package com.fitcode.fitcode_api.dto;

import lombok.Data;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class UserUpdateDto {

    @NotBlank
    private String fullName;

    private String displayName;

    @Email
    @NotBlank
    private String email; // si permites cambiar email ten cuidado con unicidad

    @NotBlank
    private String password; // si se envía, se actualizará (hash)

    @NotBlank
    private LocalDate dateOfBirth;

    @NotBlank
    private String sex;

    private Integer heightCm;

    private Double weightKg;

    private String metadata; // JSON u otro string

    private Long roleId; // si deseas actualizar el role (necesitarás RoleRepository)

}
