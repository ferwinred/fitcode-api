package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRoutineRepository extends JpaRepository<UserRoutine, Long> {
    // Define custom query methods if needed
    List<UserRoutine> findByUserId(Long userId);

    List<UserRoutine> findByRoutineId(Long routineId);

    Optional<UserRoutine> findByUserIdAndRoutineId(Long userId, Long routineId);

}
