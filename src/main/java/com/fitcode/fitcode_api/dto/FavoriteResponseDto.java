package com.fitcode.fitcode_api.dto;

import com.fitcode.fitcode_api.enums.FavoriteType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteResponseDto {

    private Long id;

    private Long targetId;

    private FavoriteType type;

    private LocalDateTime createdAt;
}
