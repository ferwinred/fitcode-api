package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Define custom query methods if needed

    Optional<AuditLog> findByAction(String action);

}
