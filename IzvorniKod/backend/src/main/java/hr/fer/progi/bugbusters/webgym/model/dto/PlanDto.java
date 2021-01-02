package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

@Data
public class PlanDto {
    private String coachUsername;
    private String description;
    private String dateFrom;
    private String dateTo;
    private Double price;
    private Boolean isTraining;
}
