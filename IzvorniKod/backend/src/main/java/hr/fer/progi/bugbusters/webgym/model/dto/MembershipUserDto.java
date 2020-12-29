package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MembershipUserDto {
    private Long id;
    private String username;
    private Date dateBegin;
    private Date dateEnd;
}
