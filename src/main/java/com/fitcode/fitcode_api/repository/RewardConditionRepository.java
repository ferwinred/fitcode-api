package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.RewardCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RewardConditionRepository extends JpaRepository<RewardCondition, Long> {
    // Define custom query methods if needed

    Optional<RewardCondition> findByConditionType(String conditionType);

}
