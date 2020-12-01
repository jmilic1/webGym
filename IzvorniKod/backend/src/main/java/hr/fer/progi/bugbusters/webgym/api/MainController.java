package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.service.UserException;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * The main controller which reads url requests
 * and responds with appropriate information for frontend service.
 *
 * @author jmilic
 */
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class MainController {
    /**
     * Service delegate
     */
    private UserService service;

    /**
     * Constructs MainController and wires it with the service.
     *
     * @param userService Service which does business logic
     */
    @Autowired
    public MainController(@Qualifier("userService") UserService userService) {
        this.service = userService;
    }

    /**
     * Returns a list of gyms in database.
     *
     * @return gyms
     */
    @GetMapping("/gymList")
    public List<Gym> getGyms() {
        return service.databaseGyms();
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

    /**
     * Adds the sent gym into the database.
     *
     * @param gym sent gym
     * @return response message
     */
    @PostMapping("/addGym")
    public String insertGym(@RequestBody Gym gym) {
        service.addGym(gym);
        return "Added gym!";
    }

    /**
     * Adds the sent user to database and logs in using the sent user.
     *
     * @param user     sent user
     * @param response response instance to set user cookies and status number
     * @return error message if register was unsuccessful
     */
    @PostMapping("/registration")
    public String registerUser(@RequestBody User user, HttpServletResponse response, HttpSession session) {
        try {
            User myUser = service.signUpUser(user);

            //setSession(session, myUser);
            addUserCookies(response, myUser);
            response.setStatus(200);

            changeRole(myUser);
            return "OK";
        } catch (UserException ex) {
            response.setStatus(400);
            return "{\"reason\": \"" + ex.getMessage() + "\" }";
        }
    }

    /**
     * Receives username and password and logs the user into the service.
     *
     * @param user     received user with username and password
     * @param response response to set cookies and status number
     * @return found user in database with given username
     */
    @PostMapping("/login")
    public User loginUser(@RequestBody User user, HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        User myUser = service.loginUser(user);

        if (myUser == null) {
            response.setStatus(400);
            return null;
        }

        //setSession(session, myUser);
        addUserCookies(response, myUser);

        changeRole(myUser);
        return myUser;
    }


    @GetMapping("/logOut")
    public void logoutUser(final HttpServletResponse response) {
        deleteCookie(response, new Cookie("username", null));
        deleteCookie(response, new Cookie("role", null));
        System.out.println("Logging out");
        changeRole(null);
    }

    /**
     * Logs the user out of the service
     *
     * @param session session to be deleted
     */
    @GetMapping("/login")
    public void doLogout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("username") || cookie.getName().equals("role")){
                deleteCookie(response, cookie);
            }
        }
        session.invalidate();
        changeRole(null);
    }

    @PostMapping("/addPlan")
    public void addPlan(@RequestBody Plan plan, HttpSession session, final HttpServletResponse response, HttpServletRequest request) {
        if (plan.getUser() == null) {

            //String username = (String) session.getAttribute("username");
            String username = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie:cookies) {
                if (cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
            }
            if (username != null) {
                service.addPlan(plan, username);
            } else {
                response.setStatus(403);
                return;
            }
        } else {
            //service.addPlan(plan, null);
        }

        response.setStatus(200);
    }

    @GetMapping("/getDietPlans")
    public List<Plan> getDietPlans(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies == null){
            System.out.println("Cookies are null");
            return null;
        }
        for (Cookie cookie:cookies){
            if ("username".equals(cookie.getName())){
                username = cookie.getValue();
            }
        }

        if (username == null) {
            return null;
        }

        return service.getUserDietPlans(username);
    }

    @GetMapping("/getWorkoutPlans")
    public List<Plan> getWorkoutPlans(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies == null){
            System.out.println("Cookies are null");
            return null;
        }
        for (Cookie cookie:cookies){
            if ("username".equals(cookie.getName())){
                username = cookie.getValue();
            }
        }

        if (username == null) {
            return null;
        }

        return service.getUserWorkoutPlans(username);
    }

    /**
     * Changes the role of current user to the role of the given user.
     *
     * @param user given user
     */
    protected static void changeRole(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>();
        if (user == null) {
            updatedAuthorities.add(new SimpleGrantedAuthority("unregistered"));
        } else {
            updatedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    private void deleteCookie(final HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void addUserCookies(HttpServletResponse response, User myUser) {
        Cookie cookie = new Cookie("username", myUser.getUsername());
        Cookie roleCookie = new Cookie("role", myUser.getRole().toString());
        cookie.setMaxAge(60);
        roleCookie.setMaxAge(60);
        response.addCookie(cookie);
        response.addCookie(roleCookie);
    }
}
