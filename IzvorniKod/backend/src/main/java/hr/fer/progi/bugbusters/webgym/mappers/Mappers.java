package hr.fer.progi.bugbusters.webgym.mappers;

import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mappers {
    private static ModelMapper modelMapper;

    @Autowired
    public Mappers(ModelMapper modelMapper) {
        Mappers.modelMapper = modelMapper;
    }

    public static User mapDtoToUser(UserDto userDto) { return modelMapper.map(userDto, User.class); }
    public static UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static Goal mapDtoToGoal(GoalDto goalDto) { return modelMapper.map(goalDto, Goal.class); }
    public static GoalDto mapGoalToDto(Goal goal) { return modelMapper.map(goal, GoalDto.class); }

    public static Gym mapDtoToGym(GymDto gymDto) { return modelMapper.map(gymDto, Gym.class); }
    public static GymDto mapGymToDto(Gym gym) { return modelMapper.map(gym, GymDto.class); }

    public static GymLocation mapDtoToLocation(GymLocationDto gymLocationDto){
        return modelMapper.map(gymLocationDto, GymLocation.class);
    }
    public static GymLocationDto mapLocationToDto(GymLocation gymLocation){
        GymLocationDto gymLocationDto = modelMapper.map(gymLocation, GymLocationDto.class);
        gymLocationDto.setId(gymLocation.getGym().getId());
        return gymLocationDto;
    }

    public static Plan mapDtoToPlan(PlanDto planDto, User user){
        Plan plan = modelMapper.map(planDto, Plan.class);
        plan.setUser(user);
        return plan;
    }
    public static PlanDto mapPlanToDto(Plan plan) {
        PlanDto dto = modelMapper.map(plan, PlanDto.class);
        dto.setCoachUsername(plan.getUser().getUsername());
        return dto;
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
