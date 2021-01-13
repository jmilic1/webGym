package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
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
    private final GoalRepository goalRepository;
    private final GymUserRepository gymUserRepository;
    private final JobRequestRepository jobRequestRepository;
    private final MembershipUserRepository membershipUserRepository;
    private final PlanClientRepository planClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementService(@Qualifier("gymRep") GymRepository gymRepository,
                                 @Qualifier("userRep") UserRepository userRepository,
                                 @Qualifier("planRep") PlanRepository planRepository,
                                 @Qualifier("goalRep") GoalRepository goalRepository,
                                 @Qualifier("gymUserRep") GymUserRepository gymUserRepository,
                                 @Qualifier("jobRequestRep") JobRequestRepository jobRequestRepository,
                                 @Qualifier("membershipUserRep") MembershipUserRepository membershipUserRepository,
                                 @Qualifier("planClientRep") PlanClientRepository planClientRepository) {
        this.gymRepository = gymRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.goalRepository = goalRepository;
        this.gymUserRepository = gymUserRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.membershipUserRepository = membershipUserRepository;
        this.planClientRepository = planClientRepository;
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
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty())
            throw new RuntimeException("Logged in user not found!");
        User user = userOptional.get();

        for (Goal goal:user.getGoals()){
            goalRepository.deleteById(goal.getId());
        }
        for (GymUser gymUser:user.getGymUserList()){
            gymUserRepository.deleteById(gymUser.getId());
        }
        for (JobRequest jobRequest: user.getJobRequests()){
            jobRequestRepository.deleteById(jobRequest.getId());
        }
        for (MembershipUser membershipUser: user.getMembershipUserList()){
            membershipUserRepository.deleteById(membershipUser.getId());
        }
        for (PlanClient planClient: user.getClientPlans()){
            planClientRepository.deleteById(planClient.getId());
        }
        for (Plan plan: user.getPlans()){
            planRepository.deleteById(plan.getId());
        }

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
