package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.WorkoutDto;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.services.WorkoutService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    @GetMapping
    public Page<Workout> list(Pageable pageable,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) Integer isPublic) {
        // Simplificado: if q present do search, else list
        if (q != null && !q.isBlank()) {
            return workoutService.list(pageable) // implement search in service if needed
                    .map(w -> w); // placeholder
        }
        return workoutService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> get(@PathVariable Long id) {
        return ResponseEntity.ok(workoutService.get(id));
    }

    @PostMapping
    public ResponseEntity<Workout> create(@Valid @RequestBody WorkoutDto dto) {
        Workout w = new Workout();
        // map dto -> entity
        w.setTitle(dto.getTitle());
        w.setDescription(dto.getDescription());
        w.setDurationSeconds(dto.getDurationSeconds());
        w.setDifficulty(dto.getDifficulty());
        w.setMainMuscleGroup(dto.getMainMuscleGroup());
        w.setEquipment(dto.getEquipment());
        w.setSets(dto.getSets());
        w.setReps(dto.getReps());
        w.setThumbnailUrl(dto.getThumbnailUrl());
        w.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
        w.setMetadata(dto.getMetadata());
        Workout created = workoutService.create(w, dto.getCategoryId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> update(@PathVariable Long id, @Valid @RequestBody WorkoutDto dto) {
        Workout p = new Workout();
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setDurationSeconds(dto.getDurationSeconds());
        p.setDifficulty(dto.getDifficulty());
        p.setMainMuscleGroup(dto.getMainMuscleGroup());
        p.setEquipment(dto.getEquipment());
        p.setSets(dto.getSets());
        p.setReps(dto.getReps());
        p.setThumbnailUrl(dto.getThumbnailUrl());
        p.setIsPublic(dto.getIsPublic());
        p.setMetadata(dto.getMetadata());
        return ResponseEntity.ok(workoutService.update(id, p, dto.getCategoryId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workoutService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // dentro de WorkoutController
    @PostMapping("/bulk")
    public ResponseEntity<List<Workout>> bulkCreate(@RequestBody List<WorkoutDto> dtos) {
        List<Workout> created = new ArrayList<>();
        for (WorkoutDto dto : dtos) {
            Workout w = new Workout();
            w.setTitle(dto.getTitle());
            w.setDescription(dto.getDescription());
            w.setDurationSeconds(dto.getDurationSeconds());
            w.setDifficulty(dto.getDifficulty());
            w.setMainMuscleGroup(dto.getMainMuscleGroup());
            w.setEquipment(dto.getEquipment());
            w.setSets(dto.getSets());
            w.setReps(dto.getReps());
            w.setThumbnailUrl(dto.getThumbnailUrl());
            w.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
            w.setMetadata(dto.getMetadata());
            Workout saved = workoutService.create(w, dto.getCategoryId());
            created.add(saved);
        }
        return ResponseEntity.ok(created);
    }

}
