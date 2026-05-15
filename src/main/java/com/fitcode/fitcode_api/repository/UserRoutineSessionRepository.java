package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.UserRoutine;
import com.fitcode.fitcode_api.models.UserRoutineSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRoutineSessionRepository extends JpaRepository<UserRoutineSession, Long> {
    // Define custom query methods if needed
    List<UserRoutineSession> findByUserRoutine(UserRoutine userRoutine);

    List<UserRoutineSession> findByUserRoutineId(Long userRoutineId);

}
