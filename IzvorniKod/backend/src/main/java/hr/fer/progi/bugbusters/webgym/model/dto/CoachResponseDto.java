package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoachResponseDto {
    private UserDto user;
    private List<PlanDto> plans;
    private List<GymDto> gyms;
}
