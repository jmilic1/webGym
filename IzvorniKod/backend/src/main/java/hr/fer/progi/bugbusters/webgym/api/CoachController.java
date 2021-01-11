package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.dto.*;
import hr.fer.progi.bugbusters.webgym.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    /**
     * Constructs CoachController and wires it with the service.
     *
     * @param coachService Service which does coach business logic
     */
    @Autowired
    public CoachController(@Qualifier("coachService") CoachService coachService) {
        this.service = coachService;
    }

    @PostMapping("/addPlan")
    public void addPlan(@RequestBody PlanDto planDto, HttpServletRequest request,
                        HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return;
        }

        try {
            service.addPlan(planDto, username);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(403);
            return;
        }

        response.setStatus(200);
    }

    @PostMapping("/modifyCoachPlan")
    public void modifyCoachPlan(@RequestBody PlanDto planDto, HttpServletRequest request,
                                HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return;
        }

        try {
            service.modifyCoachPlan(planDto, username);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            response.setStatus(400);
            return;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(403);
            return;
        }
        response.setStatus(200);
    }

    @GetMapping("/coachPlan")
    public PlanDto getCoachPlan(@RequestBody PlanDto planDto, HttpServletResponse response) {
        try {
            PlanDto dto = service.getCoachPlan(planDto.getId());
            response.setStatus(200);
            return dto;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(400);
            return null;
        }
    }

    @PostMapping("/jobRequest")
    public void postJobRequest(@RequestBody JobRequestDto jobRequestDto, HttpServletRequest request,
                               HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("COACH")) {
            response.setStatus(403);
            return;
        }

        try {
            service.addJobRequest(username, jobRequestDto);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @GetMapping("/coach")
    public CoachResponseDto getCoach(@RequestParam String username, HttpServletResponse response) {
        try {
            CoachResponseDto coachResponseDto = service.getCoach(username);
            response.setStatus(200);
            return coachResponseDto;
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }

        return null;
    }
}
