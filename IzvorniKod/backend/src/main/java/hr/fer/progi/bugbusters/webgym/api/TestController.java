package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    private UserService service;

    /**
     * Constructs MainController and wires it with the service.
     *
     * @param userService Service which returns the Gym instances
     */
    @Autowired
    public TestController(@Qualifier("userService") UserService userService) {
        this.service = userService;
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
        User user = new User(false, false, true);
        MainController.changeRole(user);
        return "Switched to user!";
    }

    @GetMapping("/switchToCoach")
    public String switchToCoach(HttpServletResponse response){
        User user = new User(true, false, false);
        user.setCoach(true);
        MainController.changeRole(user);
        return "Switched to coach!";
    }

    @GetMapping("/switchToOwner")
    public String switchToOwner(HttpServletResponse response){
        User user = new User(false, true, false);
        user.setGymOwner(true);
        MainController.changeRole(user);
        return "Switched to owner!";
    }

    @GetMapping("/switchToUnregistered")
    public String switchToUnregistered(HttpServletResponse response){
        MainController.changeRole(null);
        return "Switched to owner!";
    }
}
