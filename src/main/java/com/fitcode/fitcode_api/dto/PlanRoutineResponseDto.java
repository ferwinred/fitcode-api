package com.fitcode.fitcode_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanRoutineResponseDto {

    private Long id;
    private Long plan;
    private Long routine;

}
