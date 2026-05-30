package com.fitcode.fitcode_api.services;

import com.fitcode.fitcode_api.dto.CreatePlanDto;
import com.fitcode.fitcode_api.dto.PlanResponseDto;
import com.fitcode.fitcode_api.dto.PlanRoutineResponseDto;
import com.fitcode.fitcode_api.dto.UserResponseDto;
import com.fitcode.fitcode_api.dto.UserRoutineDto;
import com.fitcode.fitcode_api.models.Plan;
import com.fitcode.fitcode_api.models.PlanRoutine;
import com.fitcode.fitcode_api.models.Routine;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.repository.PlanRepository;
import com.fitcode.fitcode_api.repository.PlanRoutineRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanRoutineRepository planRoutineRepository;
    private final RoutineService routineService;
    private final UserService userService;

    public Page<PlanResponseDto> list(Pageable p) {

        return planRepository.findAll(p).map(this::toDto);
    }

    public PlanResponseDto get(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + id));

        return toDto(plan);
    }

    public PlanResponseDto create(Plan r, List<Long> routineIds, Long userId) {

        UserResponseDto author = userService.getUserById(userId);

        User userEntity = new User();
        userEntity.setId(author.getId());
        userEntity.setEmail(author.getEmail());
        userEntity.setFullName(author.getFullName());

        r.setUser(userEntity);

        Plan savedPlan = planRepository.save(r);

        // Create associations between the plan and routines
        for (Long routineId : routineIds) {

            Routine routine = routineService.getEntity(routineId);

            PlanRoutine association = new PlanRoutine();
            association.setPlan(savedPlan);
            association.setRoutine(routine);

            planRoutineRepository.save(association);

            UserRoutineDto urd = new UserRoutineDto();
            urd.setRoutineId(routineId);
            urd.setUserId(userId);
            urd.setStartDate(LocalDate.now());
            urd.setStatus("active");

            userService.assignRoutine(userId, routineId, urd);
        }

        return toDto(savedPlan);
    }

    public PlanResponseDto update(Long id, CreatePlanDto payload) {
        Plan r = planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + id));

        r.setTitle(payload.getTitle());
        r.setDescription(payload.getDescription());
        r.setDifficulty(payload.getDifficulty());
        r.setIsPublic(payload.getIsPublic());
        r.setMetadata(payload.getMetadata());

        for (Long routineId : payload.getRoutineIds()) {
            PlanRoutine existingAssociation = planRoutineRepository.findByPlanIdAndRoutineId(id, routineId)
                    .orElse(null);
            if (existingAssociation == null) {
                Routine routine = routineService.getEntity(routineId);

                PlanRoutine association = new PlanRoutine();
                association.setPlan(r);
                association.setRoutine(routine);

                planRoutineRepository.save(association);
            }
        }

        return toDto(planRepository.save(r));
    }

    public void delete(Long id) {
        planRepository.deleteById(id);
    }

    public PlanResponseDto toDto(Plan r) {

        return new PlanResponseDto(
                r.getId(),
                r.getTitle(),
                r.getDescription(),
                r.getDifficulty(),
                r.getIsPublic(),
                r.getUser().getId(),
                r.getMetadata(),
                r.getThumbnailUrl(),
                r.getCreatedAt(),
                r.getUpdatedAt(),
                r.getPlanRoutines().stream().map(this::toPlanRoutineDto).toList());
    }

    public PlanRoutineResponseDto toPlanRoutineDto(PlanRoutine pr) {
        return new PlanRoutineResponseDto(
                pr.getId(),
                pr.getPlan().getId(),
                pr.getRoutine().getId());
    }
}
