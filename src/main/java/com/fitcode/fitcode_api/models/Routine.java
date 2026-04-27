package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "routines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String difficulty;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_user_id")
    private User author;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Integer isPublic = 1;

    @Column(columnDefinition = "LONGTEXT")
    private String metadata;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
