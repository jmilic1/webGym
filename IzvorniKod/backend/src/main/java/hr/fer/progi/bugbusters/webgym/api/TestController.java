package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Role;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.service.TestService;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    private UserService service;
    private TestService testService;

    private Boolean called = false;

    /**
     * Constructs MainController and wires it with the service.
     *
     * @param userService Service which returns the Gym instances
     */
    @Autowired
    public TestController(@Qualifier("userService") UserService userService, @Qualifier("testService") TestService testService) {
        this.service = userService;
        this.testService = testService;
    }

    @GetMapping("/")
    public String getString(){
        return "response";
    }

    @GetMapping("/testAuthorization/coach")
    public String coachSite(){
        return "This should only be available to coaches";
    }

    @GetMapping("/testAuthorization/owner")
    public String ownerSite(){
        return "This should only be available to owners";
    }

    @GetMapping("/testAuthorization/user")
    public String userSite(){
        return "This should only be available to users";
    }

    @GetMapping("/testAuthorization/unregistered")
    public String unregisteredSite(){
        return "This should be available to everyone";
    }

    @GetMapping("/switchToUser")
    public String switchToUser(HttpServletResponse response){
        User user = new User();
        user.setRole(Role.CLIENT);
        MainController.changeRole(user);
        return "Switched to user!";
    }

    @GetMapping("/switchToCoach")
    public String switchToCoach(HttpServletResponse response){
        User user = new User();
        user.setRole(Role.COACH);
        MainController.changeRole(user);
        return "Switched to coach!";
    }

    @GetMapping("/switchToOwner")
    public String switchToOwner(HttpServletResponse response){
        User user = new User();
        user.setRole(Role.OWNER);
        MainController.changeRole(user);
        return "Switched to owner!";
    }

    @GetMapping("/switchToUnregistered")
    public String switchToUnregistered(HttpServletResponse response){
        MainController.changeRole(null);
        return "Switched to owner!";
    }

    @GetMapping("/populateDatabase")
    public String populate(){
        if (!called){
            testService.populate();
            called = true;
            return "Database populated!";
        } else {
         return "This url was already called!";
        }
    }

    @GetMapping("/logInAsCoach")
    public String logInAsCoach(final HttpServletResponse resp){
        User user = testService.logInAsCoach();
        resp.addCookie(new Cookie("username", user.getUsername()));
        resp.addCookie(new Cookie("role", "COACH"));
        return "Logged in!";
    }
}
