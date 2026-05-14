package com.fitcode.fitcode_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Email(message = "Debe ser un correo valido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    private String password;
}
