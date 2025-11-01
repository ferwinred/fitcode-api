package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {
    // Define custom query methods if needed
    Optional<Reward> findByTitle(String title);

    Optional<Reward> findByDescription(String description);

}
