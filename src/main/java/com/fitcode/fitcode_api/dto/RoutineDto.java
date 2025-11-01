package com.fitcode.fitcode_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class RoutineDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer durationMinutes;
    private Integer isPublic;
    private String metadata;
    private Long authorUserId;
    private List<RoutineWorkoutDto> workouts; // optional in detail
}
