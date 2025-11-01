package com.fitcode.fitcode_api.dto;

import lombok.Data;

@Data
public class RoutineWorkoutDto {
    private Long id;
    private Long routineId;
    private Long workoutId;
    private Integer position;
    private Integer sets;
    private String reps;
    private Integer restSeconds;
    private Integer durationSeconds;
    private String notes;
}
