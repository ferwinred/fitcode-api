package com.fitcode.fitcode_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class RoutineDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer durationMinutes;
    private Integer isPublic;
    private Long authorUserId;
    private String metadata;
    // private List<RoutineWorkoutDto> workouts; // optional in detail

}
