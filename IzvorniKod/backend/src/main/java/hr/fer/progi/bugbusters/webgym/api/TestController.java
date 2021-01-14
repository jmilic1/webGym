package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.service.TestService;
import hr.fer.progi.bugbusters.webgym.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    private TestService testService;

    private Boolean called = false;

    @Autowired
    public TestController(@Qualifier("testService") TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/")
    public String getString(){
        return "response";
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

    @GetMapping("/logInAsUser")
    public String logInAsUser(final HttpServletResponse resp){
        User user = testService.logInAsUser();
        resp.addCookie(new Cookie("username", user.getUsername()));
        resp.addCookie(new Cookie("role", "CLIENT"));
        resp.setStatus(200);
        return "Logged in!";
    }

    @GetMapping("/logInAsOwner")
    public String logInAsOwner(final HttpServletResponse resp){
        User user = testService.logInAsOwner();
        resp.addCookie(new Cookie("username", user.getUsername()));
        resp.addCookie(new Cookie("role", "OWNER"));
        resp.setStatus(200);
        return "Logged in!";
    }

    @GetMapping("/logInAsAdmin")
    public String logInAsAdmin(final HttpServletResponse resp){
        User user = testService.logInAsAdmin();
        resp.addCookie(new Cookie("username", user.getUsername()));
        resp.addCookie(new Cookie("role", "ADMIN"));
        resp.setStatus(200);
        return "Logged in!";
    }
}
