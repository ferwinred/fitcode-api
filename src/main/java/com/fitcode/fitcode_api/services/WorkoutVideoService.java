package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.models.WorkoutVideo;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.repository.WorkoutVideoRepository;
import com.fitcode.fitcode_api.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutVideoService {
    private final WorkoutVideoRepository repo;
    private final WorkoutRepository workoutRepository;

    public Page<WorkoutVideo> list(Pageable p) {
        Page<WorkoutVideo> page = repo.findAll(p);
        return page;
    }

    public WorkoutVideo get(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Video not found"));
    }

    @Transactional
    public WorkoutVideo create(WorkoutVideo payload, Long workoutId) {
        if (workoutId != null) {
            Workout w = workoutRepository.findById(workoutId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout not found: " + workoutId));
            payload.setWorkout(w);
        }
        return repo.save(payload);
    }

    @Transactional
    public WorkoutVideo update(Long id, WorkoutVideo payload, Long workoutId) {
        WorkoutVideo exist = get(id);
        exist.setTitle(payload.getTitle());
        exist.setUrl(payload.getUrl());
        exist.setDurationSeconds(payload.getDurationSeconds());
        exist.setIsPublic(payload.getIsPublic());
        exist.setVideoType(payload.getVideoType());
        exist.setResolution(payload.getResolution());
        if (workoutId != null) {
            Workout w = workoutRepository.findById(workoutId)
                    .orElseThrow(() -> new EntityNotFoundException("Workout not found: " + workoutId));
            exist.setWorkout(w);
        } else {
            exist.setWorkout(null);
        }
        return repo.save(exist);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
