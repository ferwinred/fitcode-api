package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.WorkoutCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExerciseCategoryRepository extends JpaRepository<WorkoutCategory, Integer> {
    Optional<WorkoutCategory> findBySlug(String slug);

    Optional<WorkoutCategory> findByName(String name);

}
