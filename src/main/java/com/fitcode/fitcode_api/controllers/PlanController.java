package com.fitcode.fitcode_api.controllers;

import com.fitcode.fitcode_api.dto.CreatePlanDto;
import com.fitcode.fitcode_api.dto.PlanResponseDto;
import com.fitcode.fitcode_api.models.Plan;
import com.fitcode.fitcode_api.models.User;
import com.fitcode.fitcode_api.services.PlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // return pageable list simplified
        return ResponseEntity.ok(planService.list(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDto> get(@PathVariable Long id) {
        PlanResponseDto plan = planService.get(id);
        return ResponseEntity.ok(plan);
    }

    @PostMapping("/manual")
    public ResponseEntity<PlanResponseDto> create(@Valid @RequestBody CreatePlanDto dto) {

        Plan r = new Plan();
        r.setTitle(dto.getTitle());
        r.setDescription(dto.getDescription());
        r.setDifficulty(dto.getDifficulty());
        r.setIsPublic(dto.getIsPublic());
        r.setMetadata(dto.getMetadata());
        r.setThumbnailUrl(dto.getThumbnailUrl());
        // set author if provided (lookup user)
        System.err.println("Creating plan for user ID: " + dto);
        PlanResponseDto created = planService.create(r, dto.getRoutineIds(), dto.getUserId());
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponseDto> update(@PathVariable Long id, @RequestBody CreatePlanDto dto) {
        CreatePlanDto p = new CreatePlanDto();
        p.setTitle(dto.getTitle());
        p.setDescription(dto.getDescription());
        p.setDifficulty(dto.getDifficulty());
        p.setIsPublic(dto.getIsPublic());
        p.setMetadata(dto.getMetadata());

        return ResponseEntity.ok(planService.update(id, p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<PlanResponseDto>> bulkCreate(@RequestBody List<CreatePlanDto> dtos) {
        List<PlanResponseDto> created = new ArrayList<>();
        for (CreatePlanDto dto : dtos) {
            Plan r = new Plan();
            r.setTitle(dto.getTitle());
            r.setDescription(dto.getDescription());
            r.setDifficulty(dto.getDifficulty());
            r.setIsPublic(dto.getIsPublic() == null ? 1 : dto.getIsPublic());
            r.setMetadata(dto.getMetadata());
            r.setThumbnailUrl(dto.getThumbnailUrl() != null ? dto.getThumbnailUrl()
                    : "https://via.placeholder.com/300x200.png?text=No+Image");
            // set author if dto.userId present; adapt according to your User repo
            if (dto.getUserId() != null) {
                User author = new User();
                author.setId(dto.getUserId());
                r.setUser(author);
            }
            created.add(planService.create(r, dto.getRoutineIds(), dto.getUserId()));
        }
        return ResponseEntity.ok(created);
    }

}
