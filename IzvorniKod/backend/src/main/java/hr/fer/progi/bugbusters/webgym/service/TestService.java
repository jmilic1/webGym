package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GoalRepository;
import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("testService")
public class TestService {
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final PlanRepository planRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TestService(@Qualifier("gymRep") GymRepository gymRepository, @Qualifier("userRep") UserRepository userRepository, @Qualifier("goalRep") GoalRepository goalRepository, @Qualifier("planRep") PlanRepository planRepository){
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.goalRepository = goalRepository;
        this.planRepository = planRepository;
    }

    public void populate(){
        populateGyms();

        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("Jeremy");
        user.setSurname("Elbertson");
        user.setUsername("jElb");
        user.setEmail("jElb@gmail.com");
        String encryptedPassword = passwordEncoder.encode("123");
        user.setPassword(encryptedPassword);
        user.setRole(Role.CLIENT);
        users.add(user);
        User jelbertson = user;

        user = new User();
        user.setName("Tomislav");
        user.setSurname("Lovrencic");
        user.setUsername("tLov");
        user.setEmail("tLov@gmail.com");
        encryptedPassword = passwordEncoder.encode("456");
        user.setPassword(encryptedPassword);
        user.setRole(Role.COACH);
        users.add(user);
        User tLov = user;

        user = new User();
        user.setName("Josip");
        user.setSurname("Jelacic");
        user.setUsername("jJel");
        user.setEmail("jJel@gmail.com");
        encryptedPassword = passwordEncoder.encode("789");
        user.setPassword(encryptedPassword);
        user.setRole(Role.OWNER);
        users.add(user);
        User jJel = user;

        userRepository.saveAll(users);

        List<Goal> goals = new ArrayList<>();
        Goal goal = new Goal(jelbertson, "Pretrci 20 kilometara", 10.0);
        goals.add(goal);

        goalRepository.saveAll(goals);

        List<Plan> plans = new ArrayList<>();
        Plan plan = new Plan();
        plan.setDateFrom(new Date(System.currentTimeMillis()));
        plan.setDateTo(new Date(System.currentTimeMillis()));
        plan.setDescription("Workout plan");
        plan.setIsTraining(true);
        plan.setPrice(25.30);
        plan.setUser(tLov);
        plans.add(plan);

        plan = new Plan();
        plan.setDateFrom(new Date(System.currentTimeMillis()));
        plan.setDateTo(new Date(System.currentTimeMillis()));
        plan.setDescription("Eating plan");
        plan.setIsTraining(false);
        plan.setPrice(99.99);
        plan.setUser(tLov);
        plans.add(plan);
        planRepository.saveAll(plans);

        tLov.setPlans(plans);
        userRepository.save(tLov);

    }

    public User logInAsCoach(){
        Optional<User> user = userRepository.findById("tLov");
        if (user.isPresent()){
            return user.get();
        } else {
            throw new RuntimeException("That username does not exist in database");
        }
    }

    private void populateGyms() {
        List<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym("Gyms4You", "Gyms but for you", "Gyms4You@Yahoo.com"));
        gyms.add(new Gym("AllAmericanGym", "Very american", "AllAmerican@gmail.com"));
        gymRepository.saveAll(gyms);
    }
}
