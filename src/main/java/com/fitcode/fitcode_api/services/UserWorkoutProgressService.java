package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.models.RoutineWorkout;
import com.fitcode.fitcode_api.models.UserRoutineSession;
import com.fitcode.fitcode_api.models.UserWorkoutProgress;
import com.fitcode.fitcode_api.models.Workout;
import com.fitcode.fitcode_api.repository.UserWorkoutProgressRepository;
import com.fitcode.fitcode_api.repository.UserRoutineSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWorkoutProgressService {
    private final UserWorkoutProgressRepository repo;
    private final UserRoutineSessionRepository sessionRepository;

    public UserWorkoutProgress save(UserWorkoutProgress p) {
        return repo.save(p);
    }

    public List<UserWorkoutProgress> listBySession(Long sessionId) {
        UserRoutineSession s = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));
        return repo.findBySession(s);
    }

    public UserWorkoutProgress get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progress not found: " + id));
    }

    public UserWorkoutProgress update(Long id, UserWorkoutProgress data) {
        UserWorkoutProgress p = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Progress not found: " + id));
        // map fields simples
        if (data.getSession() != null) {
            UserRoutineSession s = new UserRoutineSession();
            s.setId(data.getSession().getId());
            p.setSession(s);
        }
        if (data.getRoutineWorkout() != null) {
            RoutineWorkout rw = new RoutineWorkout();
            rw.setId(data.getRoutineWorkout().getId());
            p.setRoutineWorkout(rw);
        }
        if (data.getWorkout() != null) {
            Workout w = new Workout();
            w.setId(data.getWorkout().getId());
            p.setWorkout(w);
        }
        p.setSetsCompleted(data.getSetsCompleted());
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
