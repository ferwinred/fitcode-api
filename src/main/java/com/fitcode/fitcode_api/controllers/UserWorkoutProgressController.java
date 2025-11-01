package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.UserWorkoutProgressDto;
import com.fitcode.fitcode_api.models.RoutineWorkout;
import com.fitcode.fitcode_api.models.UserRoutineSession;
import com.fitcode.fitcode_api.models.UserWorkoutProgress;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.services.UserWorkoutProgressService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-workout-progress")
@RequiredArgsConstructor
public class UserWorkoutProgressController {
    private final UserWorkoutProgressService service;

    @PostMapping
    public ResponseEntity<UserWorkoutProgress> create(@RequestBody UserWorkoutProgressDto dto) {
        UserWorkoutProgress p = new UserWorkoutProgress();
        // map fields...
        return ResponseEntity.ok(service.save(p));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> bySession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(service.listBySession(sessionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<UserWorkoutProgress>> bulkCreate(@RequestBody List<UserWorkoutProgressDto> dtos) {
        List<UserWorkoutProgress> created = new ArrayList<>();
        for (UserWorkoutProgressDto dto : dtos) {
            UserWorkoutProgress p = new UserWorkoutProgress();
            // map fields simples
            if (dto.getSessionId() != null) {
                UserRoutineSession s = new UserRoutineSession();
                s.setId(dto.getSessionId());
                p.setSession(s);
            }
            if (dto.getRoutineWorkoutId() != null) {
                RoutineWorkout rw = new RoutineWorkout();
                rw.setId(dto.getRoutineWorkoutId());
                p.setRoutineWorkout(rw);
            }
            if (dto.getWorkoutId() != null) {
                Workout w = new Workout();
                w.setId(dto.getWorkoutId());
                p.setWorkout(w);
            }
            p.setSetsCompleted(dto.getSetsCompleted());
            p.setRepsDetail(dto.getRepsDetail());
            p.setWeightUsed(dto.getWeightUsed());
            p.setDurationSeconds(dto.getDurationSeconds());
            p.setNotes(dto.getNotes());
            created.add(service.save(p));
        }
        return ResponseEntity.ok(created);
    }

}