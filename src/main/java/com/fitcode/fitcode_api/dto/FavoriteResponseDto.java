package com.fitcode.fitcode_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FavoriteResponseDto {

    private Long id;

    private Long target_id;

    private Long user_id;

    private String type;

    private LocalDateTime created_at;
}
