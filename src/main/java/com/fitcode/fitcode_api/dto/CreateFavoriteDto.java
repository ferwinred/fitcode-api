package com.fitcode.fitcode_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFavoriteDto {

    @NotNull
    private Long targetId;

    @NotNull
    private String type;

}