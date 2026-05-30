package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.UserResponseDto;
import com.fitcode.fitcode_api.dto.UserRoutineDto;
import com.fitcode.fitcode_api.dto.UserUpdateDto;
import com.fitcode.fitcode_api.exceptions.ResourceNotFoundException;
import com.fitcode.fitcode_api.models.Reward;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.Streak;
import com.fitcode.fitcode_api.models.UserReward;
import com.fitcode.fitcode_api.models.UserRoutine;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.repository.StreakRepository;
import com.fitcode.fitcode_api.repository.UserRewardRepository;
import com.fitcode.fitcode_api.repository.UserRoutineRepository;
import com.fitcode.fitcode_api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRoutineRepository userRoutineRepository;
    private final StreakRepository streakRepository;
    private final UserRewardRepository userRewardRepository;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponseDto> getByEmail(@RequestParam("email") String email) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        try {
            UserResponseDto updated = userService.updateUser(id, dto);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error al actualizar usuario: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(@PathVariable Long id) {
        try {
            userService.softDeleteUser(id);
        } catch (ResourceNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/{id}/routines")
    public ResponseEntity<List<Map<String, Object>>> getUserRoutines(@PathVariable Long id) {
        return ResponseEntity.ok(userRoutineRepository.findByUserId(id).stream()
                .map(this::toUserRoutineView)
                .toList());
    }

    @GetMapping("/{id}/streak")
    public ResponseEntity<Map<String, Object>> getUserStreak(@PathVariable Long id) {
        Streak streak = streakRepository.findByUserId(id).stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Racha no encontrada"));
        return ResponseEntity.ok(toStreakView(streak));
    }

    @GetMapping("/{id}/rewards")
    public ResponseEntity<List<Map<String, Object>>> getUserRewards(@PathVariable Long id) {
        return ResponseEntity.ok(userRewardRepository.findAllByUserId(id).stream()
                .map(this::toUserRewardView)
                .toList());
    }

    @PostMapping("/{id}/routines/{routineId}/assign")
    public ResponseEntity<UserRoutineDto> assignRoutine(@PathVariable Long id, @PathVariable Long routineId,
            @RequestBody UserRoutineDto data) {
        UserRoutineDto userRoutine = userService.assignRoutine(id, routineId, data);
        return ResponseEntity.ok(userRoutine);
    }

    private Map<String, Object> toUserRoutineView(UserRoutine userRoutine) {
        Routine routine = userRoutine.getRoutine();
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", userRoutine.getId());
        view.put("user_id", userRoutine.getUser().getId());
        view.put("routine_id", routine.getId());
        view.put("routine", toRoutineView(routine));
        view.put("start_date", userRoutine.getStartDate());
        view.put("end_date", userRoutine.getEndDate());
        view.put("progress_percent", userRoutine.getProgressPercent() == null ? 0 : userRoutine.getProgressPercent());
        view.put("status", userRoutine.getStatus());
        view.put("created_at", userRoutine.getCreatedAt());
        view.put("updated_at", userRoutine.getUpdatedAt());
        view.put("thumbnail_url", routine.getThumbnailUrl());
        view.put("metadata", routine.getMetadata());
        return view;
    }

    private Map<String, Object> toRoutineView(Routine routine) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", routine.getId());
        view.put("title", routine.getTitle());
        view.put("description", routine.getDescription());
        view.put("difficulty", routine.getDifficulty());
        view.put("duration_minutes", routine.getDurationMinutes());
        view.put("author_user_id", routine.getAuthor() == null ? null : routine.getAuthor().getId());
        view.put("is_public", routine.getIsPublic() == null || routine.getIsPublic() == 1);
        view.put("metadata", routine.getMetadata());
        view.put("created_at", routine.getCreatedAt());
        view.put("updated_at", routine.getUpdatedAt());
        view.put("workout_count", routine.getWorkouts().size());
        view.put("workouts", routine.getWorkouts().stream().map(rw -> {
            Workout workout = rw.getWorkout();
            Map<String, Object> workoutView = new LinkedHashMap<>();
            workoutView.put("id", workout.getId());
            workoutView.put("title", workout.getTitle());
            workoutView.put("description", workout.getDescription());
            workoutView.put("duration_minutes", workout.getDurationSeconds());
            workoutView.put("metadata", workout.getMetadata());
            workoutView.put("created_at", workout.getCreatedAt());
            workoutView.put("difficulty", workout.getDifficulty());
            workoutView.put("thumbnail_url", workout.getThumbnailUrl());
            workoutView.put("category", workout.getCategory());
            workoutView.put("reps", workout.getReps());
            workoutView.put("sets", workout.getSets());

            return workoutView;
        }).toList());
        view.put("categories", List.of());
        view.put("rating", 0);
        view.put("is_free", routine.getIsPublic() == null || routine.getIsPublic() == 1);
        view.put("thumbnail_url", routine.getThumbnailUrl());
        return view;
    }

    private Map<String, Object> toStreakView(Streak streak) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", streak.getId());
        view.put("user_id", streak.getUser().getId());
        view.put("name", streak.getName());
        view.put("current_streak", streak.getCurrentStreak() == null ? 0 : streak.getCurrentStreak());
        view.put("longest_streak", streak.getLongestStreak() == null ? 0 : streak.getLongestStreak());
        view.put("last_date", streak.getLastDate());
        view.put("created_at", streak.getCreatedAt());
        view.put("updated_at", streak.getUpdatedAt());
        return view;
    }

    private Map<String, Object> toUserRewardView(UserReward userReward) {
        Reward reward = userReward.getReward();
        Map<String, Object> rewardView = new LinkedHashMap<>();
        rewardView.put("id", reward.getId());
        rewardView.put("code", reward.getCode());
        rewardView.put("title", reward.getTitle());
        rewardView.put("description", reward.getDescription());
        rewardView.put("reward_type", reward.getRewardType());
        rewardView.put("metadata", reward.getMetadata());
        rewardView.put("created_at", reward.getCreatedAt());

        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", userReward.getId());
        view.put("user_id", userReward.getUser().getId());
        view.put("reward_id", reward.getId());
        view.put("reward", rewardView);
        view.put("awarded_at", userReward.getAwardedAt());
        view.put("redeemed_at", userReward.getRedeemedAt());
        view.put("metadata", userReward.getMetadata());
        view.put("created_at", userReward.getCreatedAt());
        return view;
    }
}
