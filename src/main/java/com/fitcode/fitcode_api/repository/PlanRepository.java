package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Plan;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    // Define custom query methods if needed
    Optional<Plan> findByTitle(String title);

    Page<Plan> findByIsPublic(Integer isPublic, Pageable pageable);

    Page<Plan> findByTitleContainingIgnoreCase(String q, Pageable pageable);

}
