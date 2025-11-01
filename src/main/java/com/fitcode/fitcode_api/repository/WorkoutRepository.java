package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.models.WorkoutCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findByTitle(String title);

    Page<Workout> findByCategory(WorkoutCategory category, Pageable pageable);

    Page<Workout> findByDifficulty(String difficulty, Pageable pageable);

    Page<Workout> findByIsPublic(Integer isPublic, Pageable pageable);

    Page<Workout> findByTitleContainingIgnoreCase(String q, Pageable pageable);
}