package com.fitcode.fitcode_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward_conditions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @Column(name = "condition_type", length = 40, nullable = false)
    private String conditionType;

    @Column(name = "condition_value", length = 255, nullable = false)
    private String conditionValue;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
