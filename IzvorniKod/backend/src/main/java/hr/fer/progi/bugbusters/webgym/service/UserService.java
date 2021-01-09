package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanClientDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import hr.fer.progi.bugbusters.webgym.model.dto.TransactionDto;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    public UserService(@Qualifier("userRep") UserRepository userRepository,
                       @Qualifier("planRep") PlanRepository planRepository,
                       @Qualifier("membershipRep") MembershipRepository membershipRepository,
                       @Qualifier("goalRep") GoalRepository goalRepository,
                       @Qualifier("planClientRep") PlanClientRepository planClientRepository,
                       @Qualifier("membershipUserRep") MembershipUserRepository membershipUserRepository){
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
        this.goalRepository = goalRepository;
        this.planClientRepository = planClientRepository;
        this.membershipUserRepository = membershipUserRepository;
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

        user.addPlanClient(planClient);
        plan.addPlanClient(planClient);

        userRepository.save(user);
        planRepository.save(plan);
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
