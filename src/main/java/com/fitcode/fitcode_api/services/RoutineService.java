package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.RoutineDto;
import com.fitcode.fitcode_api.dto.WorkoutDto;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.RoutineWorkout;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.repository.RoutineRepository;
import com.fitcode.fitcode_api.repository.RoutineWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineWorkoutRepository routineWorkoutRepository;

    public Page<RoutineDto> list(Pageable p) {
        return routineRepository.findAll(p).map(this::toDto);
    }

    public RoutineDto get(Long id) {
        return toDto(routineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found: " + id)));
    }

    public Routine create(Routine r) {
        return routineRepository.save(r);
    }

    public Page<RoutineDto> listByDifficulty(String difficulty, Pageable p) {
        return routineRepository.findByDifficultyIgnoreCase(difficulty, p).map(this::toDto);
    }

    public Routine update(Long id, Routine payload) {
        Routine r = getEntity(id);
        r.setTitle(payload.getTitle());
        r.setDescription(payload.getDescription());
        r.setDurationMinutes(payload.getDurationMinutes());
        r.setDifficulty(payload.getDifficulty());
        r.setIsPublic(payload.getIsPublic());
        r.setMetadata(payload.getMetadata());
        return routineRepository.save(r);
    }

    public void delete(Long id) {
        routineRepository.deleteById(id);
    }

    public List<RoutineWorkout> getRoutineWorkouts(Long routineId) {
        Routine r = getEntity(routineId);
        return routineWorkoutRepository.findByRoutineOrderByPositionAsc(r);
    }

    public RoutineDto toDto(Routine r) {
        return new RoutineDto(
                r.getId(),
                r.getTitle(),
                r.getDescription(),
                r.getDifficulty(),
                r.getDurationMinutes(),
                r.getIsPublic(),
                r.getAuthor().getId(),
                toWorkoutDto(getRoutineWorkouts(r.getId())),
                r.getMetadata(),
                r.getThumbnailUrl(),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }

    public List<WorkoutDto> toWorkoutDto(List<RoutineWorkout> routinesWorkouts) {
        return routinesWorkouts.stream().map(rw -> {
            Workout workout = rw.getWorkout();
            WorkoutDto w = new WorkoutDto();
            w.setId(workout.getId());
            w.setTitle(workout.getTitle());
            w.setDescription(workout.getDescription());
            w.setDurationSeconds(workout.getDurationSeconds());
            w.setDifficulty(workout.getDifficulty());
            w.setThumbnailUrl(workout.getThumbnailUrl());
            w.setCategoryId(workout.getCategory().getId());
            w.setReps(workout.getReps());
            w.setSets(workout.getSets());
            return w;
        }).toList();
    }

    public Routine getEntity(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found: " + id));
    }
}
