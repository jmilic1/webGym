package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Goal;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.GoalDto;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
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
import java.util.List;
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
    private UserService service;
    private ModelMapper modelMapper;

    /**
     * Constructs UserController and wires it with the service.
     *
     * @param userService Service which does user business logic
     */
    @Autowired
    public UserController(@Qualifier("userService") UserService userService, ModelMapper modelMapper) {
        this.service = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Returns a list of users in database.
     *
     * @return users
     */
    @GetMapping("/userList")
    public List<User> getUsers() {
        return service.listUsers();
    }

    @PostMapping("/modifyUserGoal")
    public void modifyGoal(@RequestBody GoalDto goalDto) {
        service.modifyGoal(modelMapper.map(goalDto, Goal.class));
    }

    @PostMapping("/addUserGoal")
    public void addGoal(@RequestBody GoalDto goalDto, HttpServletRequest request){
        String username = extractUsernameFromCookies(request);
        service.addGoal(modelMapper.map(goalDto, Goal.class), username);
    }

    @GetMapping("/getDietPlans")
    public List<Plan> getDietPlans(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return service.getUserDietPlans(username);
    }

    @GetMapping("/getWorkoutPlans")
    public List<Plan> getWorkoutPlans(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return service.getUserWorkoutPlans(username);
    }

    @GetMapping("/getUserGoals")
    public List<GoalDto> getUserGoals(HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        if (username == null) {
            return null;
        }

        return service.getUserGoals(username).stream()
                .map(this::convertGoalToDto)
                .collect(Collectors.toList());
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

    private GoalDto convertGoalToDto(Goal goal) {
        return modelMapper.map(goal, GoalDto.class);
    }
}
