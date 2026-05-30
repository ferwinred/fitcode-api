package com.fitcode.fitcode_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanDto {

    @NotNull
    private String title;

    private String description;

    @NotNull
    private Long userId;

    @NotNull
    private String difficulty;

    @NotNull
    private Integer isPublic;

    private String metadata;

    private String thumbnailUrl;

    @NotNull
    private List<Long> routineIds;

}