package com.fitcode.fitcode_api.dto;

import com.fitcode.fitcode_api.models.Plan;
import com.fitcode.fitcode_api.models.Routine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanRoutineResponseDto {

    private Long id;
    private Plan plan;
    private Routine routine;

}
