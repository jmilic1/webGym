package hr.fer.progi.bugbusters.webgym.mappers;

import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mappers {
    private static ModelMapper modelMapper;

    @Autowired
    public Mappers(ModelMapper modelMapper) {
        Mappers.modelMapper = modelMapper;
    }

    public static User mapDtoToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
    public static UserDto mapUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static Goal mapDtoToGoal(GoalDto goalDto, User user) {
        Goal goal = modelMapper.map(goalDto, Goal.class);
        goal.setUser(user);
        return goal;
    }
    public static GoalDto mapGoalToDto(Goal goal) {
        return modelMapper.map(goal, GoalDto.class);
    }
    public static Goal mapToGoal(Goal goal, GoalDto dto, User user) {
        if (dto.getDescription() != null) goal.setDescription(dto.getDescription());
        if (dto.getPercentCompleted() != null) goal.setPercentCompleted(dto.getPercentCompleted());
        if (user != null) goal.setUser(user);

        return goal;
    }

    public static Gym mapDtoToGym(GymDto gymDto) {
        return modelMapper.map(gymDto, Gym.class);
    }

    public static GymDto mapGymToDto(Gym gym) {
        return modelMapper.map(gym, GymDto.class);
    }

    public static GymLocation mapDtoToLocation(GymLocationDto dto, Gym gym) {
        GymLocation gymLocation = new GymLocation();
        gymLocation.setCountry(dto.getCountry());
        gymLocation.setCity(dto.getCity());
        gymLocation.setStreet(dto.getStreet());
        gymLocation.setPhoneNumber(dto.getPhoneNumber());
        gymLocation.setGym(gym);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = sdf.parse(dto.getOpensAt()).getTime();
            gymLocation.setOpensAt(new Time(ms));

            ms = sdf.parse(dto.getClosesAt()).getTime();
            gymLocation.setClosesAt(new Time(ms));
        } catch (ParseException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        return gymLocation;
    }

    public static GymLocationDto mapLocationToDto(GymLocation gymLocation) {
        GymLocationDto gymLocationDto = modelMapper.map(gymLocation, GymLocationDto.class);
        gymLocationDto.setId(gymLocation.getGym().getId());
        return gymLocationDto;
    }

    public static Membership mapDtoToMembership(MembershipDto dto, Gym gym) {
        String description = dto.getDescription();
        Double price = dto.getPrice();
        String interval = dto.getInterval();

        Membership membership = new Membership();
        membership.setGym(gym);
        membership.setDescription(description);
        if (price == null) membership.setPrice((double) 0);
            else membership.setPrice(price);
        membership.setInterval(interval);
        return membership;
    }

    public static MembershipDto mapMembershipToDto(Membership membership) {
        return modelMapper.map(membership, MembershipDto.class);
    }

    public static MembershipUser mapDtoToMembershipUser(Membership membership, User user) {
        MembershipUser membershipUser = new MembershipUser();
        String interval = membership.getInterval();

        if (interval != null) {
            membershipUser.setDateBegin(new Date());
            membershipUser.setDateEnd(new Date(membershipUser.getDateBegin().getTime() + Integer.parseInt(interval)));
        }

        membershipUser.setMembership(membership);
        membershipUser.setUser(user);
        return membershipUser;
    }

    public static Plan mapDtoToPlan(PlanDto planDto, User user) {
        Plan plan = modelMapper.map(planDto, Plan.class);
        plan.setUser(user);
        return plan;
    }

    public static PlanDto mapPlanToDto(Plan plan) {
        PlanDto dto = modelMapper.map(plan, PlanDto.class);
        dto.setCoachUsername(plan.getUser().getUsername());
        return dto;
    }

    public static JobRequest mapDtoToJobRequest(JobRequestDto dto, User user, Gym gym) {
        JobRequest jobRequest = modelMapper.map(dto, JobRequest.class);
        jobRequest.setUser(user);
        jobRequest.setGym(gym);
        return jobRequest;
    }

    public static JobRequestDto mapJobRequestToDto(JobRequest jobRequest) {
        CoachDto coachDto = modelMapper.map(jobRequest.getUser(), CoachDto.class);
        Gym gym = jobRequest.getGym();
        JobRequestDto dto = modelMapper.map(jobRequest, JobRequestDto.class);

        dto.setCoach(coachDto);
        dto.setGymId(gym.getId());
        dto.setGymName(gym.getName());

        return dto;
    }

    public static CoachResponseDto mapCoachToResponseDto(User coach) {
        CoachResponseDto coachResponseDto = new CoachResponseDto();

        coachResponseDto.setUser(Mappers.mapUserToDto(coach));

        coachResponseDto.setPlans(coach.
                getPlans().stream()
                .map(Mappers::mapPlanToDto)
                .collect(Collectors.toList()));

        List<GymDto> gymDtoList = new ArrayList<>();
        for (GymUser gymUser : coach.getGymUserList()) {
            gymDtoList.add(mapGymToDto(gymUser.getGym()));
        }
        coachResponseDto.setGyms(gymDtoList);

        return coachResponseDto;
    }

    public static TransactionDto mapToTransactionDtoFromPlanClient(String name, Plan plan,
                                                                   PlanClient planClient,
                                                                   TransactionType transactionType) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderUsername(name);
        transactionDto.setReceiverUsername(plan.getUser().getUsername());
        transactionDto.setAmount(plan.getPrice());
        transactionDto.setDateWhen(planClient.getDateBought());
        transactionDto.setId(plan.getId());
        transactionDto.setTransactionType(transactionType);

        return transactionDto;
    }

    public static TransactionDto mapToTransactionDtoFromMembership(String name,
                                                                   Membership membership,
                                                                   MembershipUser membershipUser,
                                                                   TransactionType transactionType) {
        String gymName = "";
        if (membership.getGym() != null && membership.getGym().getName() != null) gymName = membership.getGym().getName();

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderUsername(name);
        transactionDto.setReceiverUsername(gymName);
        transactionDto.setAmount(membership.getPrice());
        transactionDto.setDateWhen(membershipUser.getDateBegin());
        transactionDto.setId(membership.getId());
        transactionDto.setTransactionType(transactionType);

        return transactionDto;
    }

}
