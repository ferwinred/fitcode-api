package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.RoutineDto;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.services.RoutineService;
import com.fitcode.fitcode_api.services.RoutineWorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {
    private final RoutineService routineService;
    private final RoutineWorkoutService routineWorkoutService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // return pageable list simplified
        return ResponseEntity.ok(routineService.list(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Routine> get(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.get(id));
    }

    @PostMapping
    public ResponseEntity<Routine> create(@Valid @RequestBody RoutineDto dto) {
        Routine r = new Routine();
        r.setTitle(dto.getTitle());
        r.setDescription(dto.getDescription());
        r.setDurationMinutes(dto.getDurationMinutes());
        r.setDifficulty(dto.getDifficulty());
        r.setIsPublic(dto.getIsPublic());
        r.setMetadata(dto.getMetadata());
        // set author if provided (lookup user)
        Routine created = routineService.create(r);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Routine> update(@PathVariable Long id, @RequestBody RoutineDto dto) {
        Routine p = new Routine();
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setDurationMinutes(dto.getDurationMinutes());
        p.setDifficulty(dto.getDifficulty());
        p.setIsPublic(dto.getIsPublic());
        p.setMetadata(dto.getMetadata());
        return ResponseEntity.ok(routineService.update(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/workouts")
    public ResponseEntity<List<?>> getRoutineWorkouts(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.getRoutineWorkouts(id));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Routine>> bulkCreate(@RequestBody List<RoutineDto> dtos) {
        List<Routine> created = new ArrayList<>();
        for (RoutineDto dto : dtos) {
            Routine r = new Routine();
            r.setTitle(dto.getTitle());
            r.setDescription(dto.getDescription());
            r.setDurationMinutes(dto.getDurationMinutes());
            r.setDifficulty(dto.getDifficulty());
            r.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
            r.setMetadata(dto.getMetadata());
            // set author if dto.authorUserId present; adapt according to your User repo
            if (dto.getAuthorUserId() != null) {
                User author = new User();
                author.setId(dto.getAuthorUserId());
                r.setAuthor(author);
            }
            created.add(routineService.create(r));
        }
        return ResponseEntity.ok(created);
    }

}
