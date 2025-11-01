package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_rewards", indexes = { @Index(columnList = "user_id, reward_id") })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    private LocalDateTime awardedAt;
    private LocalDateTime redeemedAt;

    @Column(columnDefinition = "json")
    private String metadata;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
