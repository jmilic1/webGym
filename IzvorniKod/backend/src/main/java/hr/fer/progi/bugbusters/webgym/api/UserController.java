package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.dto.*;
import hr.fer.progi.bugbusters.webgym.service.AdminService;
import hr.fer.progi.bugbusters.webgym.service.CoachService;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User controller which reads url requests
 * and responds with appropriate information for frontend service.
 */
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class UserController {
    /**
     * User service delegate
     */
    private UserService userService;
    private CoachService coachService;
    private AdminService adminService;

    /**
     * Constructs UserController and wires it with the service.
     *
     * @param userService Service which does user business logic
     */
    @Autowired
    public UserController(@Qualifier("userService") UserService userService,
                          @Qualifier("coachService") CoachService coachService,
                          @Qualifier("adminService") AdminService adminService) {
        this.userService = userService;
        this.coachService = coachService;
        this.adminService = adminService;
    }

    /**
     * Returns a list of users in database.
     *
     * @return users
     */
    @GetMapping("/userList")
    public List<UserDto> getUsers(HttpServletRequest request, HttpServletResponse response) {
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || (!role.equals("ADMIN") && !role.equals("OWNER"))) {
            response.setStatus(403);
            return null;
        }

        return userService.listUsers();
    }

    @PostMapping("/modifyUserGoal")
    public void modifyGoal(@RequestBody GoalDto goalDto, HttpServletRequest request,
                           HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return;
        }

        try {
            userService.modifyGoal(goalDto, username);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @GetMapping("/getUserGoals")
    public List<GoalDto> getUserGoals(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return null;
        }

        try {
            return userService.getUserGoals(username);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(403);
            return null;
        }
    }

    @PostMapping("/addUserGoal")
    public void addGoal(@RequestBody GoalDto goalDto, HttpServletRequest request,
                        HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return;
        }

        try {
            userService.addGoal(goalDto, username);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(403);
        }
    }

    @PostMapping("/userPlans")
    public void userBuysPlan(@RequestBody PlanClientDto planClientDto,
                             HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return;
        }

        if (userService.addPlanClientConnection(username, planClientDto)) response.setStatus(200);
        else response.setStatus(400);
    }

    @GetMapping("/userPlans")
    public List<PlanDto> getUserPlans(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (username == null || role == null) {
            response.setStatus(403);
            return null;
        }

        // role == COACH
        if (role.equals("COACH")) {
            List<PlanDto> coachPlans = coachService.getAllCoachPlans(username);
            if (coachPlans == null) response.setStatus(403);
            else response.setStatus(200);
            return coachPlans;
        }

        // role == ADMIN
        if (role.equals("ADMIN")) {
            List<PlanDto> allPlans = adminService.getAllPlans();
            response.setStatus(200);
            return allPlans;
        }

        // role == CLIENT
        if (role.equals("CLIENT")) {
            List<PlanDto> userPlans = userService.getAllUserPlans(username);
            if (userPlans == null) System.out.println("Null");
            if (userPlans == null) response.setStatus(403);
            else response.setStatus(200);
            return userPlans;
        }

        response.setStatus(403);
        return null;
    }

    @GetMapping("/myTransactions")
    public List<TransactionDto> getMyTransactions(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (username == null || role == null) {
            response.setStatus(403);
            return null;
        }

        List<TransactionDto> transactionDtoList = userService.getMyTransactions(username, role);
        if (transactionDtoList == null) response.setStatus(400);
        else response.setStatus(200);
        return transactionDtoList;
    }

    @GetMapping("/getDietPlans")
    public List<PlanDto> getDietPlans(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return null;
        }

        try {
            return userService.getUserDietPlans(username);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            response.setStatus(403);
            return null;
        }
    }

    @GetMapping("/getWorkoutPlans")
    public List<PlanDto> getWorkoutPlans(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return null;
        }

        try {
            return userService.getUserWorkoutPlans(username);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            response.setStatus(403);
            return null;
        }
    }

    @PostMapping("/buyMembership")
    public void buyMembership(@RequestBody MembershipDto membershipDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return;
        }

        try {
            userService.buyMembership(username, membershipDto.getId());
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @PostMapping("/buyPlan")
    public void buyPlan(@RequestBody PlanDto planDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("CLIENT")) {
            response.setStatus(403);
            return;
        }

        try {
            userService.buyPlan(username, planDto.getId());
            response.setStatus(200);
        } catch (Exception e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @GetMapping("/user")
    public UserDto getUser(@RequestParam String username, HttpServletRequest request, HttpServletResponse response) {
        String logedUsername = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || (!role.equals("ADMIN") && !role.equals("COACH"))) {
            response.setStatus(403);
            return null;
        }

        try {
            UserDto userDto = userService.getUser(username, logedUsername);
            response.setStatus(200);
            return userDto;
        } catch (Exception e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
            return null;
        }
    }

    @GetMapping("/myClients")
    public List<UserDto> getMyClients(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return null;
        }

        try {
            List<UserDto> userDtoList = userService.getMyClients(username);
            response.setStatus(200);
            return userDtoList;
        } catch (Exception e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
            return null;
        }
    }

    @GetMapping("/owners")
    public List<UserDto> getOwners(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return null;
        }

        try {
            List<UserDto> userDtoList = userService.getOwners(username);
            response.setStatus(200);
            return userDtoList;
        } catch (Exception e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
            return null;
        }
    }
}
