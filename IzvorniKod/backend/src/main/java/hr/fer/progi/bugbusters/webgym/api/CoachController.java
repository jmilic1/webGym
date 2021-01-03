package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Goal;
import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.dto.PlanDto;
import hr.fer.progi.bugbusters.webgym.service.CoachService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Coach controller which reads url requests
 * and responds with appropriate information for frontend service.
 */
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class CoachController {

    /**
     * Coach service delegate
     */
    private CoachService service;
    private ModelMapper modelMapper;

    /**
     * Constructs CoachController and wires it with the service.
     *
     * @param coachService Service which does coach business logic
     */
    @Autowired
    public CoachController(@Qualifier("coachService") CoachService coachService, ModelMapper modelMapper) {
        this.service = coachService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/addPlan")
    public void addPlan(@RequestBody PlanDto planDto, HttpServletRequest request) {
        String username = extractUsernameFromCookies(request);

        service.addPlan(modelMapper.map(planDto, Plan.class), username);
    }

    private String extractUsernameFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                if ("username".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
