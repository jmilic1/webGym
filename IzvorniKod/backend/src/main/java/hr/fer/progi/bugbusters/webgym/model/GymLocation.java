package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class GymLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Gym gym;
    private String country;
    private String city;
    private String street;
    private Time opensAt;
    private Time closesAt;
    private String phoneNumber;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gymLocation")
    private List<JobRequest> jobRequests;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gymLocation")
    private List<GymUser> gymUserList;
}
