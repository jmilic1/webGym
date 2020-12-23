package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

@Data
public class GymDto {
    private Long id;
    private String name;
    private String description;
    private String email;
}
