package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "workout_videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout; // nullable allowed

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 1024)
    private String url;

    private Integer durationSeconds;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Integer isPublic = 1;

    @Column(length = 40)
    private String videoType; // 'tonificacion','zumba', etc.

    private String resolution;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
