package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.Routine;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    // Define custom query methods if needed
    Optional<Routine> findByTitle(String title);

    Page<Routine> findByIsPublic(Integer isPublic, Pageable pageable);

    Page<Routine> findByTitleContainingIgnoreCase(String q, Pageable pageable);

}
