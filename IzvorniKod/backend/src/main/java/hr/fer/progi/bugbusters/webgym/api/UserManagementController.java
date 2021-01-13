package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.mappers.Mappers;
import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import hr.fer.progi.bugbusters.webgym.service.UserException;
import hr.fer.progi.bugbusters.webgym.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User controller which reads url requests
 * and responds with appropriate information for frontend service.
 */
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class UserManagementController {
    /**
     * User service delegate
     */
    private UserManagementService service;

    /**
     * Constructs UserController and wires it with the service.
     *
     * @param userManagementService Service which does user business logic
     */
    @Autowired
    public UserManagementController(@Qualifier("userManagementService") UserManagementService userManagementService) {
        this.service = userManagementService;
    }

    /**
     * Adds the sent user to database and logs in using the sent user.
     *
     * @param userDto  sent user
     * @param response response instance to set user cookies and status number
     * @return error message if register was unsuccessful
     */
    @PostMapping("/registration")
    public String registerUser(@RequestBody UserDto userDto, HttpServletResponse response) {
        try {
            User myUser = service.signUpUser(userDto);

            ControllerHelper.addUserCookies(response, myUser);
            response.setStatus(200);

            return "OK";
        } catch (UserException ex) {
            response.setStatus(400);
            return "{\"reason\": \"" + ex.getMessage() + "\" }";
        }
    }

    /**
     * Receives username and password and logs the user into the service.
     *
     * @param userDto  received user with username and password
     * @param response response to set cookies and status number
     * @return found user in database with given username
     */
    @PostMapping("/login")
    public UserDto loginUser(@RequestBody UserDto userDto, HttpServletResponse response) {
        User myUser = service.loginUser(userDto);

        if (myUser == null) {
            response.setStatus(400);
            return null;
        }

        ControllerHelper.addUserCookies(response, myUser);
        return Mappers.mapUserToDto(myUser);
    }

    @DeleteMapping("/modifyUser")
    public void deleteUser(HttpServletResponse response, HttpServletRequest request) {
        String username = ControllerHelper.deleteUser(request, response);
        service.deleteUser(username);
    }

    @GetMapping("/logOut")
    public void logoutUser(final HttpServletResponse response, HttpServletRequest request) {
        ControllerHelper.logOutUser(request, response);
    }

    @GetMapping("/login")
    public void doLogout(HttpServletResponse response, HttpServletRequest request) {
        ControllerHelper.logOutUser(request, response);
    }

    @PostMapping("/modifyUser")
    public void modifyUser(@RequestBody UserDto userDto, HttpServletResponse response, HttpServletRequest request) {
        try {
            String username = ControllerHelper.extractUsernameFromCookies(request);
            service.modifyUser(userDto, username);
        } catch (UserException ex) {
            response.setStatus(403);
        }
    }
}
