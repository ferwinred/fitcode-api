package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.CreateFavoriteDto;
import com.fitcode.fitcode_api.dto.FavoriteResponseDto;
import com.fitcode.fitcode_api.enums.FavoriteType;

import java.util.List;

public interface FavoriteService {

    FavoriteResponseDto addFavorite(
            Long userId,
            CreateFavoriteDto dto);

    void removeFavorite(
            Long userId,
            Long targetId,
            FavoriteType type);

    List<FavoriteResponseDto> getFavorites(
            Long userId);

    List<FavoriteResponseDto> getFavoritesByType(
            Long userId,
            FavoriteType type);

    boolean isFavorite(
            Long userId,
            Long targetId,
            FavoriteType type);
}