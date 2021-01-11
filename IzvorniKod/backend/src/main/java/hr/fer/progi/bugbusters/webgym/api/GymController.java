package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.dto.*;
import hr.fer.progi.bugbusters.webgym.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class GymController {
    private GymService service;

    @Autowired
    public GymController(@Qualifier("gymService") GymService gymService) {
        this.service = gymService;
    }

    /**
     * Returns a list of gyms in database.
     *
     * @return gyms
     */
    @GetMapping("/gymList")
    public List<GymDto> getGyms() {
        return service.listGyms();
    }

    /**
     * Adds the sent gym into the database.
     *
     * @param gymDto sent gym
     */
    @PostMapping("/addGym")
    public void insertGym(@RequestBody GymDto gymDto, HttpServletRequest request,
                          HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.addGym(username, gymDto);
            response.setStatus(200);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            response.setStatus(403);
        }

    }

    @GetMapping("/myGyms")
    public List<GymDto> getMyGyms(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);

        if (role == null || (!role.equals("OWNER") && !role.equals("COACH"))) {
            response.setStatus(403);
            return null;
        }

        try {
            List<GymDto> gymDtoList = service.getMyGyms(username);
            response.setStatus(200);
            return gymDtoList;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(403);
            return null;
        }
    }

    @DeleteMapping("/myGyms")
    public void deleteMyGym(@RequestBody GymDto gymDto, HttpServletRequest request,
                            HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.deleteMyGym(gymDto.getId(), username);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @DeleteMapping("/gymLocation")
    public void deleteGymLocation(@RequestBody GymLocationDto gymLocationDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.deleteGymLocation(gymLocationDto.getId(), username);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @PostMapping("/gymLocation")
    public void updateGymLocation(@RequestBody GymLocationDto gymLocationDto,
                                  HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.updateGymLocation(gymLocationDto, username);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @GetMapping("/allJobRequests")
    public List<JobRequestDto> getAllJobRequests(HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return null;
        }

        try {
            List<JobRequestDto> jobRequestDtoList = service.getAllJobRequests(username);
            response.setStatus(200);
            return jobRequestDtoList;
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
            return null;
        }
    }

    @PostMapping("/allJobRequests")
    public void responseForJobRequest(@RequestBody JobResponseDto jobResponseDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.responseForJobRequest(jobResponseDto, username);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @PostMapping("/addGymOwner")
    public void addGymOwner(@RequestBody AddGymOwnerDto addGymOwnerDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        try {
            service.addGymOwner(addGymOwnerDto, username);
            response.setStatus(200);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
        }
    }

    @GetMapping("/gymInfo")
    public GymInfoDto getGymLocation(@RequestParam long id) {
        return service.getGymInfo(id);
    }

    @PostMapping("/gymInfo")
    public void createGymLocation(@RequestBody GymLocationDto gymLocationDto, HttpServletRequest request, HttpServletResponse response) {
        String username = ControllerHelper.extractUsernameFromCookies(request);
        String role = ControllerHelper.extractRoleFromCookies(request);
        if (role == null || !role.equals("OWNER")) {
            response.setStatus(403);
            return;
        }

        service.createGymLocation(gymLocationDto, username);
    }

    @GetMapping("/membership")
    public MembershipDto getMembership(@RequestParam long id, HttpServletResponse response) {
        try {
            return service.getMembership(id);
        } catch (IllegalArgumentException e) {
            response.setStatus(Integer.parseInt(e.getMessage()));
            return null;
        }
    }

    @PostMapping("/membership")
    public void createMembership(@RequestBody MembershipDto membershipDto) {
        service.createMembership(membershipDto);
    }
}
