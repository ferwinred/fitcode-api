package com.fitcode.fitcode_api.repository;

import com.fitcode.fitcode_api.models.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRewardRepository extends JpaRepository<UserReward, Long> {
    // Define custom query methods if needed

    Optional<UserReward> findByUserIdAndRewardId(Long userId, Long rewardId);

    Optional<UserReward> findByUserIdAndRewardCode(Long userId, String rewardCode);

    Optional<UserReward> findByUserId(Long userId);

    Optional<UserReward> findByRewardId(Long rewardId);

}
