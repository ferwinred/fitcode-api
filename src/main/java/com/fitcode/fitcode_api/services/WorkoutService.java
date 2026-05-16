package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.models.WorkoutCategory;
import com.fitcode.fitcode_api.repository.WorkoutRepository;
import com.fitcode.fitcode_api.repository.WorkoutCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutCategoryRepository categoryRepository;

    public Page<Workout> list(Pageable pageable) {
        return workoutRepository.findAll(pageable);
    }

    public Workout get(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found: " + id));
    }

    @Transactional
    public Workout create(Workout w, Integer categoryId) {
        if (categoryId != null) {
            WorkoutCategory cat = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
            w.setCategory(cat);
        }
        return workoutRepository.save(w);
    }

    @Transactional
    public Workout update(Long id, Workout payload, Integer categoryId) {
        Workout exist = get(id);
        exist.setTitle(payload.getTitle());
        exist.setDescription(payload.getDescription());
        exist.setDurationSeconds(payload.getDurationSeconds());
        exist.setDifficulty(payload.getDifficulty());
        exist.setMainMuscleGroup(payload.getMainMuscleGroup());
        exist.setEquipment(payload.getEquipment());
        exist.setSets(payload.getSets());
        exist.setReps(payload.getReps());
        exist.setThumbnailUrl(payload.getThumbnailUrl());
        exist.setIsPublic(payload.getIsPublic());
        exist.setMetadata(payload.getMetadata());
        if (categoryId != null) {
            WorkoutCategory cat = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryId));
            exist.setCategory(cat);
        }
        return workoutRepository.save(exist);
    }

    public void delete(Long id) {
        workoutRepository.deleteById(id);
    }

}
