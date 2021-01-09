package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.GymDto;
import hr.fer.progi.bugbusters.webgym.model.dto.JobRequestDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import hr.fer.progi.bugbusters.webgym.model.dto.TransactionDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("coachService")
public class CoachService {

    UserRepository userRepository;
    PlanRepository planRepository;
    GymRepository gymRepository;
    PlanClientRepository planClientRepository;
    GymUserRepository gymUserRepository;
    JobRequestRepository jobRequestRepository;
    ModelMapper modelMapper;

    @Autowired
    public CoachService(@Qualifier("userRep") UserRepository userRepository,
                        @Qualifier("planRep") PlanRepository planRepository,
                        @Qualifier("gymRep") GymRepository gymRepository,
                        @Qualifier("planClientRep") PlanClientRepository planClientRepository,
                        @Qualifier("gymUserRep") GymUserRepository gymUserRepository,
                        @Qualifier("jobRequestRep") JobRequestRepository jobRequestRepository,
                        ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.gymRepository = gymRepository;
        this.planClientRepository = planClientRepository;
        this.gymUserRepository = gymUserRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.modelMapper = modelMapper;
    }

    public void addPlan(Plan plan, String username) throws RuntimeException {
        if (username == null) throw new RuntimeException("User is not logged in!");

        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()){
            User myUser = user.get();

            if (myUser.getRole() != Role.COACH) {
                throw new RuntimeException("Plan can be added only by coach!");
            }

            plan.setUser(myUser);
            planRepository.save(plan);
        }
    }

    public void modifyCoachPlan(PlanDto planDto, String username) {
        if (username == null) throw new RuntimeException("User is not logged in!");
        if (planDto.getId() == null) throw new RuntimeException("Plan ID was not given!");

        Optional<User> user = userRepository.findById(username);
        Optional<Plan> plan = planRepository.findById(planDto.getId());

        if (user.isPresent() && plan.isPresent()){
            User myUser = user.get();
            Plan myPlan = plan.get();

            if (myUser.getRole() != Role.COACH) {
                throw new RuntimeException("Plan can be added only by coach!");
            }
            if (!myPlan.getUser().getUsername().equals(username)) {
                throw new RuntimeException("Coach is trying to edit others coach plan!");
            }

            myPlan.setDescription(planDto.getDescription());
            planRepository.save(myPlan);
        }
    }

    public List<PlanDto> getAllCoachPlans(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isEmpty()) return null;
        User user = optionalUser.get();

        return planRepository.findByUser(user).stream().map(plan -> Mappers.mapPlanToPlanDto(plan)).collect(Collectors.toList());
    }

    public Plan getCoachPlan(Long id) {
        if (id == null) return null;

        Optional<Plan> plan = planRepository.findById(id);

        if (plan.isPresent()) {
            Plan myPlan = plan.get();

            return myPlan;
        }

        return null;
    }

    public void addJobRequest(String username, JobRequestDto jobRequestDto) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.COACH) throw new IllegalArgumentException("404");

        Optional<Gym> optionalGym = gymRepository.findById(jobRequestDto.getGymId());
        if (optionalGym.isEmpty()) throw new IllegalArgumentException("404");
        Gym gym = optionalGym.get();

        JobRequest jobRequest = Mappers.mapDtoToJobRequest(jobRequestDto, user, gym);
        jobRequestRepository.save(jobRequest);
    }

}
