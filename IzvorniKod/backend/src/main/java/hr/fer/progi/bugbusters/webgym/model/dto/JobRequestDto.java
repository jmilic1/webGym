package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

@Data
public class JobRequestDto {
    private Long id;
    private String username;
    private String description;
    private Boolean approved;
}
