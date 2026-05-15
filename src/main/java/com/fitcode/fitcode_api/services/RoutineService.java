package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.RoutineDto;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.RoutineWorkout;
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

    public Routine get(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found: " + id));
    }

    public Routine create(Routine r) {
        return routineRepository.save(r);
    }

    public Routine update(Long id, Routine payload) {
        Routine r = get(id);
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
        Routine r = get(routineId);
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
                r.getMetadata(),
                r.getThumbnailUrl(),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }
}
