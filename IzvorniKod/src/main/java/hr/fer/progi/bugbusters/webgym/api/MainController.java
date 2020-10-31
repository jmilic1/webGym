package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.City;
import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller of the application which can receive a GET request on URL "/".
 * After receiving the request the controller uses its service to create and return instances of the City model.
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
     * @param userService Service which returns the City instances
     */
    @Autowired
    public MainController(@Qualifier("userService") UserService userService) {
        this.service = userService;
    }

    /**
     * This method is called upon recieving a GET request on URL "/".
     * Calls the UserService to create hardcoded Cities.
     *
     * @return list of Cities
     */
    @GetMapping("/")
    public List<City> getAllCities() {
        return service.createCities();
    }
}
