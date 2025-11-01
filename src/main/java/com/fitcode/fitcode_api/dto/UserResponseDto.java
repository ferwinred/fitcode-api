package com.fitcode.fitcode_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String displayName;
    private String email;
    private LocalDate dateOfBirth;
    private String sex;
    private Integer heightCm;
    private Double weightKg;
    private String metadata;
    private Long role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
