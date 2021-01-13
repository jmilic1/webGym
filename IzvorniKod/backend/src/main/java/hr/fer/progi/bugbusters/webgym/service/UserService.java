package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.*;
import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.*;
import hr.fer.progi.bugbusters.webgym.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserService(@Qualifier("userRep") UserRepository userRepository,
                       @Qualifier("planRep") PlanRepository planRepository,
                       @Qualifier("membershipRep") MembershipRepository membershipRepository,
                       @Qualifier("goalRep") GoalRepository goalRepository,
                       @Qualifier("planClientRep") PlanClientRepository planClientRepository,
                       @Qualifier("membershipUserRep") MembershipUserRepository membershipUserRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.membershipRepository = membershipRepository;
        this.goalRepository = goalRepository;
        this.planClientRepository = planClientRepository;
        this.membershipUserRepository = membershipUserRepository;
    }

    public List<UserDto> listUsers() {
        Iterable<User> it = userRepository.findAll();
        List<User> users = new ArrayList<>();

        for (User user : it) {
            users.add(user);
        }
        return users
                .stream()
                .map(Mappers::mapUserToDto)
                .collect(Collectors.toList());
    }

    public List<PlanDto> getUserDietPlans(String username) {
        return getSpecificPlans(username, Plan::getIsTraining);
    }

    public List<PlanDto> getUserWorkoutPlans(String username) {
        return getSpecificPlans(username, plan -> !plan.getIsTraining());
    }

    public List<GoalDto> getUserGoals(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Goal> goals = user.getGoals();

            return goals.stream()
                    .map(Mappers::mapGoalToDto)
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Currently logged in user not found!");
        }
    }

    public void buyMembership(String username, Long membershipId) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");

        User user = optionalUser.get();
        if (user.getRole() != Role.CLIENT) throw new IllegalArgumentException("403");

        Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        if (optionalMembership.isEmpty()) throw new IllegalArgumentException("403");
        Membership membership = optionalMembership.get();

        MembershipUser membershipUser = Mappers.mapDtoToMembershipUser(membership, user);
        membershipUserRepository.save(membershipUser);
    }

    public void buyPlan(String username, Long planId) {
        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("404");

        User user = optionalUser.get();
        if (user.getRole() != Role.CLIENT) throw new IllegalArgumentException("403");

        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isEmpty()) throw new IllegalArgumentException("403");
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
        if (logedUser.getRole() != Role.COACH && logedUser.getRole() != Role.ADMIN)
            throw new IllegalArgumentException("403");

        Optional<User> optionalUser = userRepository.findById(username);
        if (optionalUser.isEmpty()) throw new IllegalArgumentException("403");
        User user = optionalUser.get();
        if (user.isEnabled()) throw new IllegalArgumentException("403");

        if (logedUser.getRole() == Role.ADMIN) return Mappers.mapUserToDto(user);
        // For coach check if user has bought his plan
        for (Plan plan : logedUser.getPlans()) {
            for (PlanClient planClient : planClientRepository.findByPlan(plan)) {
                if (planClient.getClient().getUsername().equals(username)) {
                    return Mappers.mapUserToDto(user);
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
        for (Plan plan : user.getPlans()) {
            for (PlanClient planClient : planClientRepository.findByPlan(plan)) {
                clientSet.add(planClient.getClient().getUsername());
            }
        }

        List<UserDto> userDtoList = new ArrayList<>();
        for (String clientUsername : clientSet) {
            Optional<User> optionalClient = userRepository.findById(clientUsername);
            optionalClient.ifPresent(value -> userDtoList.add(Mappers.mapUserToDto(value)));
        }

        return userDtoList;
    }

    public List<UserDto> getOwners() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user: users) {
            if (user.getRole() == Role.OWNER)
                userDtos.add(Mappers.mapUserToDto(user));
        }

        return userDtos;
    }

    public void modifyGoal(GoalDto dto, String username) {
        Optional<User> userOptional = userRepository.findById(username);
        if (userOptional.isEmpty())
            throw new IllegalArgumentException("403");

        Optional<Goal> goalOptional = goalRepository.findById(dto.getId());
        if (goalOptional.isEmpty()) throw new IllegalArgumentException("400");

        if (!goalOptional.get().getUser().getUsername().equals(username))
            throw new IllegalArgumentException("403");

        Goal goal = Mappers.mapToGoal(goalOptional.get(), dto, userOptional.get());
        goalRepository.save(goal);
    }

    public void addGoal(GoalDto dto, String username) {
        Optional<User> user = userRepository.findById(username);

        if (user.isPresent()) {
            User myUser = user.get();
            Goal goal = Mappers.mapDtoToGoal(dto, myUser);
            goalRepository.save(goal);
        } else {
            throw new RuntimeException("Logged in user not found in database!");
        }
    }

    public List<PlanDto> getAllUserPlans(String username) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<PlanClient> planClients = user.getClientPlans();

            List<Plan> plans = new ArrayList<>();
            for (PlanClient planClient : planClients) {
                Optional<Plan> optionalPlan = planRepository.findById(planClient.getId());
                optionalPlan.ifPresent(plans::add);
            }

            return plans.stream()
                    .map(Mappers::mapPlanToDto)
                    .collect(Collectors.toList());
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

    private List<PlanDto> getSpecificPlans(String username, Predicate<Plan> remove) {
        Optional<User> optionalUser = userRepository.findById(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Plan> plans = user.getPlans();

            plans.removeIf(remove);

            return plans.stream()
                    .map(Mappers::mapPlanToDto)
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Currently logged in user not found!");
        }
    }

    // ZA SADA IMPLEMENTIRANO SAMO ZA PLANOVE -> TREBA DODATI I ZA MEMBERSHIPOVE
    public List<TransactionDto> getMyTransactions(String username, String role) {
        List<TransactionDto> transactions = new ArrayList<>();

        List<PlanClient> planClients = planClientRepository.findAll();

        for (PlanClient planClient : planClients) {
            Plan plan = planClient.getPlan();
            User user = planClient.getClient();

            if (role.equals("COACH") && !plan.getUser().getUsername().equals(username)) continue;
            if (role.equals("CLIENT") && !user.getUsername().equals(username)) continue;

            TransactionDto transactionDto = Mappers.mapToTransactionDtoFromPlanClient(user.getUsername(),
                    plan, planClient, TransactionType.PLAN);

            transactions.add(transactionDto);
        }
        if (role.equals("COACH")) {
            return transactions;
        }

        List<MembershipUser> membershipUsers = membershipUserRepository.findAll();

        for (MembershipUser membershipUser : membershipUsers) {
            Membership membership = membershipUser.getMembership();
            User user = membershipUser.getUser();

            if (!user.getUsername().equals(username)) continue;

            TransactionDto transactionDto = Mappers.mapToTransactionDtoFromMembership(user.getUsername(),
                    membership, membershipUser, TransactionType.MEMBERSHIP);

            transactions.add(transactionDto);
        }

        return transactions;
    }
}
