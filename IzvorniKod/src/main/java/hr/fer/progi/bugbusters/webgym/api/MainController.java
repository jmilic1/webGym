package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller of the application which can receive a GET request on URL "/".
 * After receiving the request the controller uses its service to create and return instances of the Gym model.
 *
 * @author jmilic
 */
@RestController
public class MainController {
    /**
     * Service delegate
     */
    private UserService service;

    /**
     * Constructs MainController and wires it with the service.
     *
     * @param userService Service which returns the Gym instances
     */
    @Autowired
    public MainController(@Qualifier("userService") UserService userService) {
        this.service = userService;
    }

    /**
     * This method is called upon recieving a GET request on URL "/".
     * Calls the UserService to create hardcoded Gyms.
     *
     * @return list of Gyms
     */
    /*
    @GetMapping("/")
    public List<Gym> getAllGyms(final HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return service.createGyms();
    }*/

    @GetMapping("/gymList")
    public List<Gym> getGymsInDatabase(final HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return service.databaseGyms();
    }

    /**
     * for testing for now
     */
    @GetMapping("/")
    public String getString(){
        return "respone";
    }
    
    @PostMapping("/addGym")
    public String insertGym(@RequestBody Gym gym){
        System.out.println("OVDJE\n\n\n" + gym.getId());
        if (!service.addGym(gym)){
            return "Gym with that id already exists";
        }
        return "Added gym!";
    }
}
