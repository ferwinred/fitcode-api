package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.models.WorkoutVideo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WorkoutVideoRepository extends JpaRepository<WorkoutVideo, Long> {
    Optional<WorkoutVideo> findByTitle(String title);

    Page<WorkoutVideo> findByWorkout(Workout workout, Pageable pageable);

    Page<WorkoutVideo> findByVideoType(String type, Pageable pageable);

    Page<WorkoutVideo> findByIsPublic(Integer isPublic, Pageable pageable);

    Page<WorkoutVideo> findByTitleContainingIgnoreCase(String q, Pageable pageable);

}