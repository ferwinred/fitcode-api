package com.fitcode.fitcode_api.dto;

import lombok.Data;

@Data
public class WorkoutDto {
    private Long id;
    private String title;
    private String description;
    private Integer durationSeconds;
    private String difficulty;
    private String mainMuscleGroup;
    private String equipment;
    private Integer sets;
    private Integer reps;
    private String thumbnailUrl;
    private Integer isPublic;
    private String metadata;
    private Integer categoryId;
}
