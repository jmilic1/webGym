package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GoalRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.Role;
import hr.fer.progi.bugbusters.webgym.model.User;
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

    public void addPlan(Plan plan, String username) {
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

}
