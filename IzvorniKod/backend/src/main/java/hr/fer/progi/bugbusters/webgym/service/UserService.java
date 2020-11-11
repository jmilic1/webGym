package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Gym;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(@Qualifier("gymRep") GymRepository gymRepository, @Qualifier("userRep") UserRepository userRepository){
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates the Gyms and returns them.
     *
     * @return created gyms
     */
    /*
    public List<Gym> createGyms() {
        List<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym(Long.valueOf(5), "American Top Team", "Zagreb"));
        gyms.add(new Gym(Long.valueOf(12312), "CrossFit", "Osijek"));
        gyms.add(new Gym(Long.valueOf(543), "Gyms4You", "Split"));
        return gyms;
    }*/

    public List<Gym> databaseGyms(){
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym:it){
            gyms.add(gym);
        }
        return gyms;
    }

    public boolean addGym(Gym gym){

       /* Gym newGym = new Gym(gym.getName(), gym.getCity(), gym.getId());
        newGym.setId(796);
        System.out.println("THE GYM IS\n OF NAME\n " + newGym.getName() + "City: " + newGym.getCity() + "and id: " + newGym.getId());
       */
        System.out.println(gym.getName());
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

}
