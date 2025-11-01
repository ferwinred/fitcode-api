package com.fitcode.fitcode_api.dto;

import lombok.Data;

@Data
public class WorkoutVideoDto {
    private Long id;
    private Long workoutId; // optional
    private String title;
    private String url;
    private Integer durationSeconds;
    private Integer isPublic;
    private String videoType;
    private String resolution;
}
