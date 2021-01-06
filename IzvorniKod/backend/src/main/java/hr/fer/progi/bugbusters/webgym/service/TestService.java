package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GoalRepository;
import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
        user.setName("Mirko");
        user.setSurname("Mirkic");
        user.setUsername("mirko");
        user.setEmail("mirko@gmail.com");
        encryptedPassword = passwordEncoder.encode("456");
        user.setPassword(encryptedPassword);
        user.setRole(Role.COACH);
        users.add(user);
        User mirko = user;

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

        user = new User();
        user.setName("Sef");
        user.setSurname("Sefic");
        user.setUsername("sef");
        user.setEmail("sef@fer.hr");
        encryptedPassword = passwordEncoder.encode("123");
        user.setPassword(encryptedPassword);
        user.setRole(Role.ADMIN);
        users.add(user);
        User sef = user;

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

        // mirko
        plan = new Plan();
        plan.setDateFrom(new Date(System.currentTimeMillis()));
        plan.setDateTo(new Date(System.currentTimeMillis()));
        plan.setDescription("Mirkov plan");
        plan.setIsTraining(false);
        plan.setPrice(99.99);
        plan.setUser(mirko);
        plans.add(plan);
        planRepository.save(plan);


        plans = new ArrayList<>();
        plans.add(plan);
        mirko.setPlans(plans);
        userRepository.save(mirko);

    }

    public User logInAsCoach(){
        Optional<User> user = userRepository.findById("tLov");
        if (user.isPresent()){
            changeRole(user.get());
            return user.get();
        } else {
            throw new RuntimeException("That username does not exist in database");
        }
    }

    public User logInAsUser(){
        Optional<User> user = userRepository.findById("jElb");
        if (user.isPresent()){
            changeRole(user.get());
            return user.get();
        } else {
            throw new RuntimeException("That username does not exist in database");
        }
    }

    public User logInAsAdmin() {
        Optional<User> user = userRepository.findById("sef");
        if (user.isPresent()){
            changeRole(user.get());
            return user.get();
        } else {
            throw new RuntimeException("That username does not exist in database");
        }
    }

    /**
     * Changes the role of current user to the role of the given user.
     *
     * @param user given user
     */
    protected static void changeRole(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        if (user == null) {
            updatedAuthorities.add(new SimpleGrantedAuthority("unregistered"));
        } else {
            updatedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private void populateGyms() {
        List<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym("Gyms4You", "Gyms but for you", "Gyms4You@Yahoo.com"));
        gyms.add(new Gym("AllAmericanGym", "Very american", "AllAmerican@gmail.com"));
        gymRepository.saveAll(gyms);
    }
}
