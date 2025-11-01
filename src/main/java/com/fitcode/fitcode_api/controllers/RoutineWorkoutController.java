package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.RoutineWorkoutDto;
import com.fitcode.fitcode_api.models.RoutineWorkout;
import com.fitcode.fitcode_api.services.RoutineWorkoutService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routine-workouts")
@RequiredArgsConstructor
public class RoutineWorkoutController {
    private final RoutineWorkoutService service;

    @PostMapping
    public ResponseEntity<RoutineWorkout> add(@Valid @RequestBody RoutineWorkoutDto dto) {
        RoutineWorkout payload = new RoutineWorkout();
        payload.setSets(dto.getSets());
        payload.setReps(dto.getReps());
        payload.setRestSeconds(dto.getRestSeconds());
        payload.setDurationSeconds(dto.getDurationSeconds());
        payload.setNotes(dto.getNotes());
        payload.setPosition(dto.getPosition());
        RoutineWorkout created = service.addToRoutine(dto.getRoutineId(), dto.getWorkoutId(), payload);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineWorkout> update(@PathVariable Long id, @RequestBody RoutineWorkoutDto dto) {
        RoutineWorkout payload = new RoutineWorkout();
        payload.setSets(dto.getSets());
        payload.setReps(dto.getReps());
        payload.setRestSeconds(dto.getRestSeconds());
        payload.setDurationSeconds(dto.getDurationSeconds());
        payload.setNotes(dto.getNotes());
        return ResponseEntity.ok(service.update(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RoutineWorkout>> bulkCreate(@RequestBody List<RoutineWorkoutDto> dtos) {
        List<RoutineWorkout> created = new ArrayList<>();
        for (RoutineWorkoutDto dto : dtos) {
            RoutineWorkout payload = new RoutineWorkout();
            payload.setSets(dto.getSets());
            payload.setReps(dto.getReps());
            payload.setRestSeconds(dto.getRestSeconds());
            payload.setDurationSeconds(dto.getDurationSeconds());
            payload.setNotes(dto.getNotes());
            payload.setPosition(dto.getPosition());
            RoutineWorkout createdRw = service.addToRoutine(dto.getRoutineId(), dto.getWorkoutId(), payload);
            created.add(createdRw);
        }
        return ResponseEntity.ok(created);
    }

}
