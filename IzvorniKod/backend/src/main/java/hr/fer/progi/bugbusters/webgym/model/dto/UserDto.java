package hr.fer.progi.bugbusters.webgym.model.dto;

import hr.fer.progi.bugbusters.webgym.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private String username;

    private String name;

    private String surname;

    private String email;

    private String password;

    private Role role;
}
