package hr.fer.progi.bugbusters.webgym.model.dto;

import hr.fer.progi.bugbusters.webgym.model.GymLocation;
import lombok.Data;

import java.util.List;

@Data
public class GymInfoDto{
    private Long id;
    private String name;
    private String description;
    private String email;
    List<GymLocationDto> gymLocationList;
    List<UserDto> coachList;
    List<MembershipDto> membershipList;
}
