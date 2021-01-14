package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;
import org.postgresql.util.PGInterval;

@Data
public class MembershipDto {
    private Long id;
    private Double price;
    private String description;
    private String interval;
}
