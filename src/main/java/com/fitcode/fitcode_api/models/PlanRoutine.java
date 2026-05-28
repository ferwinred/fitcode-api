package com.fitcode.fitcode_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_routines")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "routine_id")
    private Routine routine;

}
