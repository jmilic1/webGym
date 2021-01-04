package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GoalRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Goal;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service("userService")
public class UserService {

    UserRepository userRepository;
    PlanRepository planRepository;
    GoalRepository goalRepository;

    @Autowired
    public UserService(@Qualifier("userRep") UserRepository userRepository, @Qualifier("planRep") PlanRepository planRepository, @Qualifier("goalRep") GoalRepository goalRepository){
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.goalRepository = goalRepository;
    }

    public List<User> listUsers() {
        Iterable<User> it = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for (User user : it) {
            users.add(user);
        }
        return users;
    }

    public List<Plan> getUserDietPlans(String username) {
        return getSpecificPlans(username, Plan::getIsTraining);
    }

    public List<Plan> getUserWorkoutPlans(String username) {
        return getSpecificPlans(username, plan -> !plan.getIsTraining());
    }

    public List<Goal> getUserGoals(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Goal> goals = user.getGoals();

            return goals;
        } else {
            throw new RuntimeException("Currently logged in user not found!");
        }
    }

    public void modifyGoal(Goal goal){
        goalRepository.save(goal);
    }

    public void addGoal(Goal goal, String username){
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()){
            User myUser = user.get();
            goal.setUser(myUser);
            goalRepository.save(goal);
        }
    }

    private List<Plan> getSpecificPlans(String username, Predicate<Plan> remove){
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Plan> plans = user.getPlans();

            plans.removeIf(remove);

            return plans;
        } else {
            throw new RuntimeException("Currently logged in user not found!");
        }
    }
}
