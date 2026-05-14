package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.RoutineWorkout;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.repository.RoutineRepository;
import com.fitcode.fitcode_api.repository.RoutineWorkoutRepository;
import com.fitcode.fitcode_api.repository.WorkoutRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineWorkoutService {
    private final RoutineWorkoutRepository repo;
    private final RoutineRepository routineRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional
    public RoutineWorkout addToRoutine(long routineId, long workoutId, RoutineWorkout payload) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new EntityNotFoundException("Workout not found"));

        // if position already exists, shift subsequent positions (simple strategy)
        Integer desiredPos = payload.getPosition();
        if (desiredPos != null) {
            final Integer finalDesiredPos = desiredPos;
            List<RoutineWorkout> existing = repo.findByRoutineOrderByPositionAsc(routine);
            existing.stream()
                    .filter(rw -> rw.getPosition() >= finalDesiredPos)
                    .forEach(rw -> rw.setPosition(rw.getPosition() + 1));
            repo.saveAll(existing);
        } else {
            // set to last + 1
            List<RoutineWorkout> list = repo.findByRoutineOrderByPositionAsc(routine);
            desiredPos = list.isEmpty() ? 1 : list.get(list.size() - 1).getPosition() + 1;
        }

        payload.setRoutine(routine);
        payload.setWorkout(workout);
        payload.setPosition(desiredPos);
        return repo.save(payload);
    }

    public void remove(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        repo.deleteById(id);
        // ideally reorder positions after deletion (left as exercise)
    }

    public List<RoutineWorkout> listByRoutine(Long routineId) {

        if (routineId == null) {
            throw new IllegalArgumentException("routineId cannot be null");
        }

        Routine r = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));
        return repo.findByRoutineOrderByPositionAsc(r);
    }

    @Transactional
    public RoutineWorkout update(Long id, RoutineWorkout payload) {

        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        RoutineWorkout exist = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
        exist.setSets(payload.getSets());
        exist.setReps(payload.getReps());
        exist.setDurationSeconds(payload.getDurationSeconds());
        exist.setRestSeconds(payload.getRestSeconds());
        exist.setNotes(payload.getNotes());
        // handle position changes if needed (complex)
        return repo.save(exist);
    }
}
