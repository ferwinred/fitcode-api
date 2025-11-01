package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.WorkoutVideoDto;
import com.fitcode.fitcode_api.models.WorkoutVideo;
import com.fitcode.fitcode_api.services.WorkoutVideoService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workout-videos")
@RequiredArgsConstructor
public class WorkoutVideoController {
    private final WorkoutVideoService service;

    @GetMapping
    public Page<WorkoutVideo> list(Pageable pageable,
            @RequestParam(required = false) Long workoutId,
            @RequestParam(required = false) String q) {
        if (workoutId != null) {
            // map to repo.findByWorkout
            return service.list(pageable); // adjust to filter if needed
        }
        return service.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutVideo> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    public ResponseEntity<WorkoutVideo> create(@RequestBody WorkoutVideoDto dto) {
        WorkoutVideo v = new WorkoutVideo();
        v.setTitle(dto.getTitle());
        v.setUrl(dto.getUrl());
        v.setDurationSeconds(dto.getDurationSeconds());
        v.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
        v.setVideoType(dto.getVideoType());
        v.setResolution(dto.getResolution());
        WorkoutVideo created = service.create(v, dto.getWorkoutId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutVideo> update(@PathVariable Long id, @RequestBody WorkoutVideoDto dto) {
        WorkoutVideo payload = new WorkoutVideo();
        payload.setTitle(dto.getTitle());
        payload.setUrl(dto.getUrl());
        payload.setDurationSeconds(dto.getDurationSeconds());
        payload.setIsPublic(dto.getIsPublic());
        payload.setVideoType(dto.getVideoType());
        payload.setResolution(dto.getResolution());
        return ResponseEntity.ok(service.update(id, payload, dto.getWorkoutId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<WorkoutVideo>> bulkCreate(@RequestBody List<WorkoutVideoDto> dtos) {
        List<WorkoutVideo> created = new ArrayList<>();
        for (WorkoutVideoDto dto : dtos) {
            WorkoutVideo v = new WorkoutVideo();
            v.setTitle(dto.getTitle());
            v.setUrl(dto.getUrl()); // espera embed url: https://www.youtube.com/embed/VIDEOID
            v.setDurationSeconds(dto.getDurationSeconds());
            v.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
            v.setVideoType(dto.getVideoType());
            v.setResolution(dto.getResolution());
            WorkoutVideo saved = service.create(v, dto.getWorkoutId());
            created.add(saved);
        }
        return ResponseEntity.ok(created);
    }

}
