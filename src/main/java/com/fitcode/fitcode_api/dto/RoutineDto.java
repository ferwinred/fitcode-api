package com.fitcode.fitcode_api.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private Integer durationMinutes;
    private Integer isPublic;
    private Long authorUserId;
    private List<WorkoutDto> workouts;
    private String metadata;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // private List<RoutineWorkoutDto> workouts; // optional in detail

}
