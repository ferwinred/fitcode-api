package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "reward_type", length = 40, nullable = false)
    private String rewardType; // badge, coins, discount, custom

    @Column(nullable = false)
    private Integer points;

    @Column(columnDefinition = "json")
    private String metadata;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
