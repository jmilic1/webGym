package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;
import org.postgresql.util.PGInterval;

@Data
public class MembershipDto {
    private Long id;
    private double price;
    private String description;
    private PGInterval interval;
}
