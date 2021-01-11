package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.dao.PlanRepository;
import hr.fer.progi.bugbusters.webgym.dao.UserRepository;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User signUpUser(UserDto dto) throws UserException {
        User user = Mappers.mapDtoToUser(dto);
        validateUser(user);
        final String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        userRepository.save(user);
        return user;
    }

    public User loginUser(UserDto dto) {
        Optional<User> myUser = userRepository.findById(dto.getUsername());
        if (myUser.isPresent()) {
            if (passwordEncoder.matches(dto.getPassword(), myUser.get().getPassword())) {
                return myUser.get();
            }
        }
        return null;
    }

    public void modifyUser(UserDto dto, String username){
        Optional<User> foundOPUser = userRepository.findById(username);
        if (foundOPUser.isEmpty()){
            throw new UserException();
        }
        User foundUser = foundOPUser.get();

        User modifiedUser = Mappers.mapDtoToUser(dto);
        if (modifiedUser.getPassword() != null){
            foundUser.setPassword(passwordEncoder.encode(modifiedUser.getPassword()));
        }
        if (modifiedUser.getEmail() != null){
            foundUser.setEmail(modifiedUser.getEmail());
        }
        if (modifiedUser.getHeight() != null){
            foundUser.setHeight(modifiedUser.getHeight());
        }
        if (modifiedUser.getWeight() != null){
            foundUser.setWeight(modifiedUser.getWeight());
        }
        if (modifiedUser.getName() != null){
            foundUser.setName(modifiedUser.getName());
        }
        if (modifiedUser.getSurname() != null){
            foundUser.setSurname(modifiedUser.getSurname());
        }
        if (modifiedUser.getPayPalAccount() != null){
            foundUser.setPayPalAccount(modifiedUser.getPayPalAccount());
        }
        if (modifiedUser.getPhoneNumber() != null){
            foundUser.setPhoneNumber(modifiedUser.getPhoneNumber());
        }

        userRepository.save(foundUser);
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
