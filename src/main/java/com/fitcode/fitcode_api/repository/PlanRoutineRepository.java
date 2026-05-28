package com.fitcode.fitcode_api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fitcode.fitcode_api.models.PlanRoutine;

public interface PlanRoutineRepository extends JpaRepository<PlanRoutine, Long> {
    // Define custom query methods if needed
    Optional<PlanRoutine> findByPlanIdAndRoutineId(Long planId, Long routineId);

    // void createAssociation(Long planId, Long routineId);

}
