package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

@Data
public class AddGymOwnerDto {
    private Long gymId;
    private String username;
}
