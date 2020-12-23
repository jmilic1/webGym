package hr.fer.progi.bugbusters.webgym.model.dto;

import lombok.Data;

import java.sql.Time;

@Data
public class GymLocationDto {
    private Long id;
    private String country;
    private String city;
    private String street;
    private Time opensAt;
    private Time closesAt;
    private String phoneNumber;
}
