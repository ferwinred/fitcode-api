package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Favorite;
import com.fitcode.fitcode_api.enums.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByUserIdAndType(Long userId, FavoriteType type);

    Optional<Favorite> findByUserIdAndTargetIdAndType(
            Long userId,
            Long targetId,
            FavoriteType type);

    boolean existsByUserIdAndTargetIdAndType(
            Long userId,
            Long targetId,
            FavoriteType type);

    void deleteByUserIdAndTargetIdAndType(
            Long userId,
            Long targetId,
            FavoriteType type);
}