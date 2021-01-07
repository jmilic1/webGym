package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

@Data
public class GoalDto {
    private Long id;
    private String description;
    private double percentCompleted;
}
