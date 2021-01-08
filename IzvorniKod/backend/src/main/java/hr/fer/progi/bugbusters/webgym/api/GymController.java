package hr.fer.progi.bugbusters.webgym.api;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.model.GymLocation;
import hr.fer.progi.bugbusters.webgym.model.Membership;
import hr.fer.progi.bugbusters.webgym.model.dto.GymDto;
import hr.fer.progi.bugbusters.webgym.model.dto.GymInfoDto;
import hr.fer.progi.bugbusters.webgym.model.dto.GymLocationDto;
import hr.fer.progi.bugbusters.webgym.model.dto.MembershipDto;
import hr.fer.progi.bugbusters.webgym.service.GymService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@RestController
public class GymController {
    private GymService service;
    private ModelMapper modelMapper;

    @Autowired
    public GymController(@Qualifier("gymService") GymService gymService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.service = gymService;
    }

    /**
     * Returns a list of gyms in database.
     *
     * @return gyms
     */
    @GetMapping("/gymList")
    public List<GymDto> getGyms() {
        return service.listGyms()
                .stream()
                .map(this::convertGymToDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds the sent gym into the database.
     *
     * @param gymDto sent gym
     * @return response message
     */
    @PostMapping("/addGym")
    public String insertGym(@RequestBody GymDto gymDto, HttpServletRequest request, HttpServletResponse response) {
        String username = extractUsernameFromCookies(request);
        if (username == null) {
            response.setStatus(403);
            return "Denied!";
        }

        Gym gym = convertGymToEntity(gymDto);
        if (!service.addGym(username, gym)) {
            response.setStatus(403);
            return "Denied!";
        }
        response.setStatus(200);
        return "Added gym!";
    }

    @GetMapping("/myGyms")
    public List<GymDto> getMyGyms(HttpServletRequest request, HttpServletResponse response) {
        String username = extractUsernameFromCookies(request);
        if (username == null) {
            response.setStatus(403);
            return null;
        }

        List<GymDto> gymDtoList = service.getMyGyms(username);
        if (gymDtoList == null) response.setStatus(403);
        else response.setStatus(200);

        return gymDtoList;
    }

    @DeleteMapping("/myGyms")
    public void deleteMyGym(@RequestBody GymDto gymDto, HttpServletRequest request, HttpServletResponse response) {
        // ovo treba brisati i sve membershipove i sve ostalo sto se naslanja na taj zapis u bazi
    }

    @GetMapping("/gymInfo")
    public GymInfoDto getGymLocation(@RequestParam long id){
        return service.getGymInfo(id);
    }

    @PostMapping("/gymInfo")
    public void createGymLocation(@RequestBody GymLocationDto gymLocationDto){
        GymLocation gymLocation = modelMapper.map(gymLocationDto, GymLocation.class);
        service.createGymLocation(gymLocation);
    }

    @GetMapping("/membership")
    public MembershipDto getMembership(@RequestParam long id){
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setId(id);

        Membership membership = service.getMembership(membershipDto.getId());
        return modelMapper.map(membership, MembershipDto.class);
    }

    @PostMapping("/membership")
    public void createMembership(@RequestBody MembershipDto membershipDto){
        Membership membership = modelMapper.map(membershipDto, Membership.class);
        service.createMembership(membership);
    }

    private Gym convertGymToEntity(GymDto gymDto) {
        return modelMapper.map(gymDto, Gym.class);
    }

    private GymDto convertGymToDto(Gym gym) {
        return modelMapper.map(gym, GymDto.class);
    }

    private GymLocation convertLocationToEntity(GymLocationDto gymLocationDto){
        return modelMapper.map(gymLocationDto, GymLocation.class);
    }

    private GymLocationDto convertLocationToDto(GymLocation gymLocation){
        GymLocationDto gymLocationDto = modelMapper.map(gymLocation, GymLocationDto.class);
        gymLocationDto.setId(gymLocation.getGym().getId());
        return gymLocationDto;
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
