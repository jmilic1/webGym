package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Goal;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.GoalDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import hr.fer.progi.bugbusters.webgym.service.AdminService;
import hr.fer.progi.bugbusters.webgym.service.CoachService;
import hr.fer.progi.bugbusters.webgym.service.UserManagementService;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private ModelMapper modelMapper;

    /**
     * Constructs UserController and wires it with the service.
     *
     * @param userService Service which does user business logic
     */
    @Autowired
    public UserController(@Qualifier("userService") UserService userService,
                          @Qualifier("coachService") CoachService coachService,
                          @Qualifier("adminService") AdminService adminService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.coachService = coachService;
        this.adminService = adminService;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns a list of users in database.
     *
     * @return users
     */
    @GetMapping("/userList")
    public List<User> getUsers() {
        return userService.listUsers();
    }

    @PostMapping("/modifyUserGoal")
    public void modifyGoal(@RequestBody GoalDto goalDto) {
        userService.modifyGoal(modelMapper.map(goalDto, Goal.class));
    }

    @GetMapping("/getUserGoals")
    public List<GoalDto> getUserGoals(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return userService.getUserGoals(username).stream()
                .map(this::convertGoalToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/addUserGoal")
    public void addGoal(@RequestBody GoalDto goalDto, HttpServletRequest request){
        String username = extractUsernameFromCookies(request);
        userService.addGoal(modelMapper.map(goalDto, Goal.class), username);
    }

    // tu idu /userPlans

    @GetMapping("/userPlans")
    public List<PlanDto> getUserPlans(HttpServletRequest request, HttpServletResponse response) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            response.setStatus(404);
            return null;
        }

        String role = extractRoleFromCookies(request);
        // role == COACH
        if (role.equals("COACH")) {
            List<PlanDto> coachPlans = coachService.getAllCoachPlans(username);
            if (coachPlans == null) response.setStatus(404);
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
            // OVO NE RADI -> TREBA SAMO OTKOMENTIRATI KADA SE SKUCA KUPNJA PLANA
            /*
            List<PlanDto> userPlans = userService.getAllUserPlans(username);
            if (userPlans == null) response.setStatus(404);
            else response.setStatus(200);
            return userPlans;
            */
            List<PlanDto> allPlans = adminService.getAllPlans();
            response.setStatus(200);
            return allPlans;
        }

        response.setStatus(404);
        return null;
    }

    @GetMapping("/getDietPlans")
    public List<Plan> getDietPlans(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return userService.getUserDietPlans(username);
    }

    @GetMapping("/getWorkoutPlans")
    public List<Plan> getWorkoutPlans(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return userService.getUserWorkoutPlans(username);
    }

    private String extractUsernameFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                if ("username".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractRoleFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                if ("role".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private GoalDto convertGoalToDto(Goal goal) {
        return modelMapper.map(goal, GoalDto.class);
    }
}
