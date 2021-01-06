package hr.fer.progi.bugbusters.webgym.mappers;

import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;

public class Mappers {

    public static PlanDto mapPlanToPlanDto(Plan plan) {
        PlanDto planDto = new PlanDto();

        planDto.setId(plan.getId());
        planDto.setCoachUsername(plan.getUser().getUsername());
        planDto.setDescription(plan.getDescription());
        planDto.setDateFrom(plan.getDateFrom().toString());
        planDto.setDateTo(plan.getDateTo().toString());
        planDto.setPrice(plan.getPrice());
        planDto.setIsTraining(plan.getIsTraining());

        return planDto;
    }

}
