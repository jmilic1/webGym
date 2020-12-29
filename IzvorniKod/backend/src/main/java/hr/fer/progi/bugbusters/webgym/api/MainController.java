package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private UserManagementService service;

    /**
     * Constructs MainController and wires it with the service.
     *
     * @param userManagementService Service which does business logic
     */
    @Autowired
    public MainController(@Qualifier("userManagementService") UserManagementService userManagementService) {
        this.service = userManagementService;
    }


}
