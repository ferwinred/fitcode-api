package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.models.UserRoutineSession;
import com.fitcode.fitcode_api.models.UserWorkoutProgress;
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

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
