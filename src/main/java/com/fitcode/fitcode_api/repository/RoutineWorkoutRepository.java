package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.RoutineWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoutineWorkoutRepository extends JpaRepository<RoutineWorkout, Long> {
    // Define custom query methods if needed
    Optional<RoutineWorkout> findByRoutineIdAndWorkoutId(Long routineId, Long workoutId);

    List<RoutineWorkout> findByRoutineOrderByPositionAsc(Routine routine);

    Optional<RoutineWorkout> findByRoutineAndPosition(Routine routine, Integer position);

    List<RoutineWorkout> findByWorkoutId(Long workoutId);

}
