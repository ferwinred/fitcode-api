package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "routine_workouts", uniqueConstraints = {
        @UniqueConstraint(name = "ux_routine_position", columnNames = { "routine_id", "position" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    private Integer position;

    private Integer sets;

    private String reps;

    private Integer restSeconds;

    private Integer durationSeconds;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
