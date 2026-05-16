package com.fitcode.fitcode_api.dto;

import com.fitcode.fitcode_api.enums.FavoriteType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFavoriteDto {

    @NotNull
    private Long targetId;

    @NotNull
    private FavoriteType type;

}