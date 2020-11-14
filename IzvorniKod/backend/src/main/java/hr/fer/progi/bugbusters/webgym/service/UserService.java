package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import javassist.Loader;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service with the name "userService" which only serves to create Gyms and return them in a list.
 *
 * @author jmilic
 */
@Service("userService")
public class UserService implements UserDetailsService {
    @Autowired
    private EmailSenderService emailSenderService;
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(@Qualifier("gymRep") GymRepository gymRepository, @Qualifier("userRep") UserRepository userRepository, @Qualifier("planRep") PlanRepository planRepository){
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    public List<Gym> databaseGyms(){
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym:it){
            gyms.add(gym);
        }
        return gyms;
    }

    public boolean addGym(Gym gym){
        gymRepository.save(gym);
        return true;
    }

    public List<User> listUsers(){
        Iterable<User> it = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for (User user:it){
            users.add(user);
        }
        return users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User with name " + username + " could not be found!");
        }
    }

    public User signUpUser(User user) throws UserException{
            validateUser(user);
            final String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            userRepository.save(user);
            //sendConfirmationMail(user.getEmail());
            return user;
    }

    private void validateUser(User user) throws UserException{
        if (user.getEmail().contains(" ") || user.getPassword().contains(" ") || user.getUsername().contains(" ")){
            throw new UserException("A field contained an illegal character!");
        }

        try {
            loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException exc){
            return;
        }


        throw new UserException("Username already taken!");
    }

    public void sendConfirmationMail(String userMail){
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Account registration!");
        mailMessage.setText("Thank you for registering to WebGym (copyright all rights reserved)! Your account was created!");
        emailSenderService.sendEmail(mailMessage);
    }

    public User loginUser(User user){
        Optional<User> myUser = userRepository.findByUsername(user.getUsername());
        if (myUser.isPresent()){
            if (passwordEncoder.matches(user.getPassword(), myUser.get().getPassword())){
                return myUser.get();
            }
        }
        return null;
    }

    public void addPlan(Plan plan, String username){
        if (username == null){
            planRepository.save(plan);
        } else {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()){
                User user = optionalUser.get();
                plan.setUser(user);
                planRepository.save(plan);
            } else {
                throw new RuntimeException("Username not found!");
            }
        }
    }

    public List<Plan> getUserDietPlans(String username){
        System.out.println("Inside service!");

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()){
            System.out.println("FOUND USER!");
            User user = optionalUser.get();
            List<Plan> plans = user.getPlans();
            for (Plan plan:plans){
                System.out.println(plan.getDescription());
            }
            plans.removeIf(p -> p.getIsWorkout());


            for (Plan plan:plans){
                //plan.getUser().setPlans(null);
            }


            return plans;
        } else {
            throw new RuntimeException("Currently logged in user not found!");
        }
    }
}
