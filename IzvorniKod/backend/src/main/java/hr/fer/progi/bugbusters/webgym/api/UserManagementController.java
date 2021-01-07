package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.User;
import hr.fer.progi.bugbusters.webgym.model.dto.UserDto;
import hr.fer.progi.bugbusters.webgym.service.UserException;
import hr.fer.progi.bugbusters.webgym.service.UserManagementService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    /**
     * Constructs UserController and wires it with the service.
     *
     * @param userManagementService Service which does user business logic
     */
    @Autowired
    public UserManagementController(@Qualifier("userManagementService") UserManagementService userManagementService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
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
        User user = convertToEntity(userDto);
        try {
            User myUser = service.signUpUser(user);

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
     * @param userDto  received user with username and password
     * @param response response to set cookies and status number
     * @return found user in database with given username
     */
    @PostMapping("/login")
    public UserDto loginUser(@RequestBody UserDto userDto, HttpServletResponse response) {
        User user = convertToEntity(userDto);
        User myUser = service.loginUser(user);

        if (myUser == null) {
            response.setStatus(400);
            return null;
        }

        addUserCookies(response, myUser);
        changeRole(myUser);
        return convertToDto(myUser);
    }

    @DeleteMapping("/modifyUser")
    public void logoutUser(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username") || cookie.getName().equals("role")) {
                if (cookie.getName().equals("username")) {
                    service.deleteUser(cookie.getValue());
                }
                deleteCookie(response, cookie);
            }
        }
        session.invalidate();
        changeRole(null);
    }

    /*
    @GetMapping("/logOut")
    public void logoutUser(final HttpServletResponse response) {
        deleteCookie(response, new Cookie("username", null));
        deleteCookie(response, new Cookie("role", null));
        changeRole(null);
    }*/

    /**
     * Logs the user out of the service
     *
     * @param session session to be deleted
     */
    @GetMapping("/login")
    public void doLogout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username") || cookie.getName().equals("role")) {
                deleteCookie(response, cookie);
            }
        }
        session.invalidate();
        changeRole(null);
    }

    @PostMapping("/modifyUser")
    public void modifyUser(@RequestBody UserDto userDto) {
        User user = convertToEntity(userDto);
        service.modifyUser(user);
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
        /*cookie.setMaxAge(600);
        roleCookie.setMaxAge(600);*/
        response.addCookie(cookie);
        response.addCookie(roleCookie);
    }

    private User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
