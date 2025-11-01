package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StreakRepository extends JpaRepository<Streak, Long> {
    // Define custom query methods if needed
    List<Streak> findByUserId(Long userId);
}
