package hr.fer.progi.bugbusters.webgym.model.dto;

import hr.fer.progi.bugbusters.webgym.model.JobRequestState;
import lombok.Data;

@Data
public class JobRequestDto {
    private Long gymId;
    private String username;
    private String description;
    private JobRequestState state;
}
