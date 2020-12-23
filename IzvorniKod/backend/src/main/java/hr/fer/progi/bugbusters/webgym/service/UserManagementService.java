package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Service with the name "userService" which only serves to create Gyms and return them in a list.
 *
 */
@Service("userManagementService")
public class UserManagementService implements UserDetailsService {
    private final GymRepository gymRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(@Qualifier("gymRep") GymRepository gymRepository, @Qualifier("userRep") UserRepository userRepository, @Qualifier("planRep") PlanRepository planRepository) {
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException("User with name " + username + " could not be found!");
        }
    }

    public User signUpUser(User user) throws UserException {
        validateUser(user);
        final String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);
        return user;
    }

    public User loginUser(User user) {
        Optional<User> myUser = userRepository.findById(user.getUsername());
        if (myUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), myUser.get().getPassword())) {
                return myUser.get();
            }
        }
        return null;
    }

    public void modifyUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(String username){
        userRepository.deleteById(username);
    }

    private void validateUser(User user) throws UserException {
        if (user.getEmail().contains(" ") || user.getPassword().contains(" ") || user.getUsername().contains(" ")) {
            throw new UserException("A field contained an illegal character!");
        }

        try {
            loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException exc) {
            return;
        }

        throw new UserException("Username already taken!");
    }
}
