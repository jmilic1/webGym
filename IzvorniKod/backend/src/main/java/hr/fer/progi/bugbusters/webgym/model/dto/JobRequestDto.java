package hr.fer.progi.bugbusters.webgym.model.dto;

import hr.fer.progi.bugbusters.webgym.model.JobRequestState;
import lombok.Data;

@Data
public class JobRequestDto {
    private Long id;
    private String description;
    private CoachDto coach;
    private Long gymId;
    private String gymName;
    private JobRequestState state;
}
