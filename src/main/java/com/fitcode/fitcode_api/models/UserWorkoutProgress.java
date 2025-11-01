package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_workout_progress", indexes = { @Index(name = "idx_session", columnList = "session_id") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWorkoutProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private UserRoutineSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_workout_id")
    private RoutineWorkout routineWorkout;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private Integer setsCompleted;

    private String repsDetail;

    private Double weightUsed;

    private Integer durationSeconds;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
