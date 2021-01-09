package hr.fer.progi.bugbusters.webgym.mappers;

import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.CoachDto;
import hr.fer.progi.bugbusters.webgym.model.dto.JobRequestDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanClientDto;
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

    public static JobRequest mapDtoToJobRequest(JobRequestDto jobRequestDto, User user, Gym gym) {
        JobRequest jobRequest = new JobRequest();
        jobRequest.setDescription(jobRequestDto.getDescription());
        jobRequest.setState(JobRequestState.IN_REVIEW);
        jobRequest.setUser(user);
        jobRequest.setGym(gym);

        return jobRequest;
    }

    public static JobRequestDto mapJobRequestToDto(JobRequest jobRequest, Gym gym, CoachDto coachDto) {
        JobRequestDto jobRequestDto = new JobRequestDto();
        jobRequestDto.setId(jobRequest.getId());
        jobRequestDto.setCoach(coachDto);
        jobRequestDto.setDescription(jobRequest.getDescription());
        jobRequestDto.setGymId(gym.getId());
        jobRequestDto.setGymName(gym.getName());
        jobRequestDto.setState(jobRequest.getState());

        return jobRequestDto;
    }

}
