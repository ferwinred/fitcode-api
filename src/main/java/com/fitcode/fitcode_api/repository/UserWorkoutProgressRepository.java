package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.UserRoutineSession;
import com.fitcode.fitcode_api.models.UserWorkoutProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserWorkoutProgressRepository extends JpaRepository<UserWorkoutProgress, Long> {
    // Define custom query methods if needed
    List<UserWorkoutProgress> findBySessionId(Long sessionId);

    List<UserWorkoutProgress> findByWorkoutId(Long workoutId);

    List<UserWorkoutProgress> findBySessionIdAndWorkoutId(Long sessionId, Long workoutId);

    List<UserWorkoutProgress> findBySession(UserRoutineSession session);

}