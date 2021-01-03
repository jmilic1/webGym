package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GoalRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.Role;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("coachService")
public class CoachService {

    UserRepository userRepository;
    PlanRepository planRepository;

    @Autowired
    public CoachService(@Qualifier("userRep") UserRepository userRepository, @Qualifier("planRep") PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
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

    public Plan getCoachPlan(Long id) {
        if (id == null) return null;

        Optional<Plan> plan = planRepository.findById(id);

        if (plan.isPresent()) {
            Plan myPlan = plan.get();

            return myPlan;
        }

        return null;
    }

}
