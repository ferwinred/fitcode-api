package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.CreateFavoriteDto;
import com.fitcode.fitcode_api.dto.FavoriteResponseDto;

import java.util.List;

public interface FavoriteService {

        FavoriteResponseDto addFavorite(
                        Long userId,
                        CreateFavoriteDto dto);

        void removeFavorite(
                        Long userId,
                        Long targetId,
                        String type);

        List<FavoriteResponseDto> getFavorites(
                        Long userId);

        List<FavoriteResponseDto> getFavoritesByType(
                        Long userId,
                        String type);

        boolean isFavorite(
                        Long userId,
                        Long targetId,
                        String type);
}