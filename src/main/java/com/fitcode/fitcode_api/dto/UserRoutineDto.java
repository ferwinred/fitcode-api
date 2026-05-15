package com.fitcode.fitcode_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoutineDto {
    private Long id;
    private Long userId;
    private Long routineId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer progressPercent;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}