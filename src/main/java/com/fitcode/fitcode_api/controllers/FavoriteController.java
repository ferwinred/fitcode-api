package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.CreateFavoriteDto;
import com.fitcode.fitcode_api.dto.FavoriteResponseDto;
import com.fitcode.fitcode_api.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{userId}")
    public FavoriteResponseDto addFavorite(
            @PathVariable Long userId,
            @RequestBody CreateFavoriteDto dto) {
        System.out.println("Received addFavorite request: userId=" + userId + ", dto=" + dto);
        return favoriteService.addFavorite(userId, dto);
    }

    @DeleteMapping("/{userId}")
    public void removeFavorite(
            @PathVariable Long userId,
            @RequestParam Long targetId,
            @RequestParam String type) {

        favoriteService.removeFavorite(
                userId,
                targetId,
                type);
    }

    @GetMapping("/{userId}")
    public List<FavoriteResponseDto> getFavorites(
            @PathVariable Long userId) {
        System.out.println("Received getFavorites request: userId=" + userId);
        return favoriteService.getFavorites(userId);
    }

    @GetMapping("/{userId}/type/{type}")
    public List<FavoriteResponseDto> getFavoritesByType(
            @PathVariable Long userId,
            @PathVariable String type) {

        return favoriteService.getFavoritesByType(
                userId,
                type);
    }

    @GetMapping("/{userId}/check")
    public boolean isFavorite(
            @PathVariable Long userId,
            @RequestParam Long targetId,
            @RequestParam String type) {

        return favoriteService.isFavorite(
                userId,
                targetId,
                type);
    }
}