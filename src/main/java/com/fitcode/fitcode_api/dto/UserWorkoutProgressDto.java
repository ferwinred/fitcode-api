package com.fitcode.fitcode_api.dto;

import lombok.Data;

@Data
public class UserWorkoutProgressDto {
    private Long id;
    private Long sessionId;
    private Long routineWorkoutId;
    private Long workoutId;
    private Integer setsCompleted;
    private String repsDetail;
    private Double weightUsed;
    private Integer durationSeconds;
    private String notes;
}
