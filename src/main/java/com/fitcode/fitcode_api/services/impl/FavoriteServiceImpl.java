package com.fitcode.fitcode_api.services.impl;

import com.fitcode.fitcode_api.dto.CreateFavoriteDto;
import com.fitcode.fitcode_api.dto.FavoriteResponseDto;
import com.fitcode.fitcode_api.models.Favorite;
import com.fitcode.fitcode_api.enums.FavoriteType;
import com.fitcode.fitcode_api.repository.FavoriteRepository;
import com.fitcode.fitcode_api.services.FavoriteService;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.repository.UserRepository;
import com.fitcode.fitcode_api.repository.WorkoutRepository;
import com.fitcode.fitcode_api.repository.RoutineRepository;
import com.fitcode.fitcode_api.repository.WorkoutVideoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

        private final FavoriteRepository favoriteRepository;
        private final UserRepository userRepository;
        private final WorkoutRepository workoutRepository; // para validar existencia
        private final RoutineRepository routineRepository; // para validar existencia
        private final WorkoutVideoRepository workoutVideoRepository; // para validar existencia

        @Override
        public FavoriteResponseDto addFavorite(
                        Long userId,
                        CreateFavoriteDto dto) {

                boolean exists = favoriteRepository.existsByUserIdAndTargetIdAndType(
                                userId,
                                dto.getTargetId(),
                                dto.getType());

                if (exists) {
                        throw new RuntimeException("Already in favorites");
                }

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found"));

                boolean existTarget = switch (dto.getType()) {

                        case WORKOUT -> workoutRepository.existsById(dto.getTargetId());

                        case ROUTINE -> routineRepository.existsById(dto.getTargetId());

                        case VIDEO -> workoutVideoRepository.existsById(dto.getTargetId());
                };

                if (!existTarget) {
                        throw new EntityNotFoundException("Target not found");
                }

                Favorite favorite = Favorite.builder()
                                .user(user)
                                .targetId(dto.getTargetId())
                                .type(dto.getType())
                                .build();

                favoriteRepository.save(favorite);

                return mapToDTO(favorite);
        }

        @Override
        public void removeFavorite(
                        Long userId,
                        Long targetId,
                        FavoriteType type) {

                favoriteRepository.deleteByUserIdAndTargetIdAndType(
                                userId,
                                targetId,
                                type);
        }

        @Override
        public List<FavoriteResponseDto> getFavorites(Long userId) {

                return favoriteRepository.findByUserId(userId)
                                .stream()
                                .map(this::mapToDTO)
                                .toList();
        }

        @Override
        public List<FavoriteResponseDto> getFavoritesByType(
                        Long userId,
                        FavoriteType type) {

                return favoriteRepository.findByUserIdAndType(userId, type)
                                .stream()
                                .map(this::mapToDTO)
                                .toList();
        }

        @Override
        public boolean isFavorite(
                        Long userId,
                        Long targetId,
                        FavoriteType type) {

                return favoriteRepository.existsByUserIdAndTargetIdAndType(
                                userId,
                                targetId,
                                type);
        }

        private FavoriteResponseDto mapToDTO(Favorite favorite) {

                return FavoriteResponseDto.builder()
                                .id(favorite.getId())
                                .targetId(favorite.getTargetId())
                                .type(favorite.getType())
                                .createdAt(favorite.getCreatedAt())
                                .build();
        }
}