package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_routines", indexes = { @Index(name = "idx_user_status", columnList = "user_id, status") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoutine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    private Integer progressPercent;

    private String status; // active, paused, completed, cancelled

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public UserRoutine(User user, Routine routine, LocalDate startDate, Integer progressPercent, String status) {
        this.user = user;
        this.routine = routine;
        this.startDate = startDate;
        this.progressPercent = progressPercent;
        this.status = status;
    }
}
