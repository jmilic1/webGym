package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanClientDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import hr.fer.progi.bugbusters.webgym.model.dto.TransactionDto;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

    UserRepository userRepository;
    PlanRepository planRepository;
    MembershipRepository membershipRepository;
    GoalRepository goalRepository;
    PlanClientRepository planClientRepository;
    MembershipUserRepository membershipUserRepository;
    ModelMapper modelMapper;

    @Autowired
    public UserService(@Qualifier("userRep") UserRepository userRepository,
                       @Qualifier("planRep") PlanRepository planRepository,
                       @Qualifier("membershipRep") MembershipRepository membershipRepository,
                       @Qualifier("goalRep") GoalRepository goalRepository,
                       @Qualifier("planClientRep") PlanClientRepository planClientRepository,
                       @Qualifier("membershipUserRep") MembershipUserRepository membershipUserRepository,
                       ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
        this.goalRepository = goalRepository;
        this.planClientRepository = planClientRepository;
        this.membershipUserRepository = membershipUserRepository;
        this.modelMapper = modelMapper;
    }

    public List<User> listUsers() {
        return userRepository.findAll();
       /* List<UserDto> users = new ArrayList<>();

        for (User user : it) {
            // sredi kasnije mapping
            UserDto userDto = new UserDto();

            userDto.setName(user.getName());
            userDto.setSurname(user.getSurname());
            userDto.setUsername(user.getUsername());
            userDto.setRole(user.getRole());
            userDto.setEmail(user.getEmail());
            userDto.setWeight(user.getWeight());
            userDto.setHeight(user.getHeight());
            userDto.setPayPalAccount(user.getPayPalAccount());

            users.add(userDto);
        }
        return users;*/
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

    public void buyMembership(String username, Long membershipId) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("404");
        User user = optionalUser.get();
        if (user.getRole() != Role.CLIENT) throw new IllegalArgumentException("403");

        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        if (optionalMembership.isEmpty()) throw new IllegalArgumentException("404");
        Membership membership = optionalMembership.get();

        MembershipUser membershipUser = new MembershipUser();
        membershipUser.setDateBegin(java.util.Date.from(Instant.now()));
        membershipUser.setDateEnd(java.util.Date.from(Instant.now())); // TU CE TREBAT DODAT INTERVAL
        membershipUser.setMembership(membership);
        membershipUser.setUser(user);

        membershipUserRepository.save(membershipUser);
    }

    public void buyPlan(String username, Long planId) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("404");
        User user = optionalUser.get();
        if (user.getRole() != Role.CLIENT) throw new IllegalArgumentException("403");

        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isEmpty()) throw new IllegalArgumentException("404");
        Plan plan = optionalPlan.get();

        PlanClient planClient = new PlanClient();
        planClient.setDateBought(Date.from(Instant.now()));
        planClient.setPlan(plan);
        planClient.setClient(user);

        planClientRepository.save(planClient);
    }

    public UserDto getUser(String username, String logedUsername) {
        Optional<User> optionalLogedUser = userRepository.findById(logedUsername);
        if (optionalLogedUser.isEmpty()) throw new IllegalArgumentException("403");
        User logedUser = optionalLogedUser.get();
        if (logedUser.getRole() != Role.COACH && logedUser.getRole() != Role.ADMIN) throw new IllegalArgumentException("403");

        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("404");
        User user = optionalUser.get();
        if (user.isEnabled()) throw new IllegalArgumentException("404");

        if (logedUser.getRole() == Role.ADMIN) return modelMapper.map(user, UserDto.class);
        // For coach check if user has bought his plan
        for (Plan plan: logedUser.getPlans()) {
            for (PlanClient planClient: planClientRepository.findByPlan(plan)) {
                if (planClient.getClient().getUsername().equals(username)) {
                    return modelMapper.map(user, UserDto.class);
                }
            }
        }

        throw new IllegalArgumentException("403");
    }

    public List<UserDto> getMyClients(String username) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.getRole() != Role.COACH) throw new IllegalArgumentException("403");

        Set<String> clientSet = new HashSet<>();
        for (Plan plan: user.getPlans()) {
            for (PlanClient planClient: planClientRepository.findByPlan(plan)) {
                clientSet.add(planClient.getClient().getUsername());
            }
        }

        List<UserDto> userDtoList = new ArrayList<>();
        for (String clientUsername: clientSet) {
            Optional<User> optionalClient = userRepository.findById(clientUsername);
            if (optionalClient.isPresent()) userDtoList.add(modelMapper.map(optionalClient.get(), UserDto.class));
        }

        return userDtoList;
    }

    public void modifyGoal(Goal goal, String username){
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty())
            throw new IllegalArgumentException("403");
        goal.setUser(userOptional.get());
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

    public List<PlanDto> getAllUserPlans(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<PlanClient> planClients = user.getClientPlans();

            List<Plan> plans = new ArrayList<>();
            for (PlanClient planClient: planClients) {
                Optional<Plan> optionalPlan = planRepository.findById(planClient.getId());
                if (optionalPlan.isPresent()) plans.add(optionalPlan.get());
            }

            return plans.stream().map(Mappers::mapPlanToPlanDto).collect(Collectors.toList());
        }

        return null;
    }

    public boolean addPlanClientConnection(String username, PlanClientDto planClientDto) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) return false;
        User user = optionalUser.get();

        Optional<Plan> optionalPlan = planRepository.findById(planClientDto.getId());
        if (optionalPlan.isEmpty()) return false;
        Plan plan = optionalPlan.get();

        PlanClient planClient = new PlanClient();
        planClient.setClient(user);
        planClient.setPlan(plan);
        planClient.setDateBought(planClientDto.getCurrentTime());

        planClientRepository.save(planClient);
        return true;
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

    // ZA SADA IMPLEMENTIRANO SAMO ZA PLANOVE -> TREBA DODATI I ZA MEMBERSHIPOVE
    public List<TransactionDto> getMyTransactions(String username, String role) {
        List<TransactionDto> transactions = new ArrayList<>();

        List<PlanClient> planClients = planClientRepository.findAll();

        for (PlanClient planClient: planClients) {
            Plan plan = planClient.getPlan();
            User user = planClient.getClient();

            if (role.equals("COACH") && !plan.getUser().getUsername().equals(username)) continue;
            if (role.equals("CLIENT") && !user.getUsername().equals(username)) continue;

            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setSenderUsername(user.getUsername());
            transactionDto.setReceiverUsername(plan.getUser().getUsername());
            transactionDto.setAmount(plan.getPrice());
            transactionDto.setDateWhen(planClient.getDateBought());
            transactionDto.setId(plan.getId());
            transactionDto.setTransactionType(TransactionType.PLAN);

            transactions.add(transactionDto);
        }

        return transactions;
    }
}
