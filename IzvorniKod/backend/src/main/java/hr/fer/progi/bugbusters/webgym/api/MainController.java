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
    public String registerUser(@RequestBody User user, HttpServletResponse response) {
        try {
            User myUser = service.signUpUser(user);

            Cookie cookie = new Cookie("username", user.getUsername());
            Cookie roleCookie = new Cookie("role", null);

            roleCookie.setValue(user.getRole().toString());

            response.addCookie(cookie);
            response.addCookie(roleCookie);
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
    public User loginUser(@RequestBody User user, final HttpServletRequest request, final HttpServletResponse response) {
        User myUser = service.loginUser(user);

        if (myUser == null) {
            response.setStatus(400);
            return null;
        }

        addUserCookies(response, myUser);

        changeRole(myUser);
        return myUser;
    }

    private void addUserCookies(HttpServletResponse response, User myUser) {
        Cookie cookie = new Cookie("username", myUser.getUsername());
        Cookie roleCookie = new Cookie("role", myUser.getRole().toString());
        response.addCookie(cookie);
        response.addCookie(roleCookie);
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
     * @param response response to remove cookies
     */
    @GetMapping("/login")
    public void doLogout(final HttpServletResponse response) {
        deleteCookie(response, new Cookie("username", null));
        deleteCookie(response, new Cookie("role", null));
        changeRole(null);
    }

    @PostMapping("/addPlan")
    public void addPlan(@RequestBody Plan plan, final HttpServletRequest request, final HttpServletResponse response){
        if (plan.getUser() == null){
            Cookie[] cookies = request.getCookies();

            for (Cookie cookie:cookies){
                if (cookie.getName().equals("username")){
                    service.addPlan(plan, cookie.getValue());
                }
            }
        } else {
            service.addPlan(plan, null);
        }

        response.setStatus(200);
    }

    @GetMapping("/getDietPlans")
    public List<Plan> getDietPlans(final HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie:cookies){
            if (cookie.getName().equals("username")){
                return service.getUserDietPlans(cookie.getValue());
            }
        }

        return null;
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

    /**
     * Deletes the given cookie from given response.
     *
     * @param response given response
     * @param cookie   given cookie
     */
    private void deleteCookie(final HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
