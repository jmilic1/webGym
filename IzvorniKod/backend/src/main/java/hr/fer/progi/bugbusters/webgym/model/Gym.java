package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NoArgsConstructor
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String email;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gym")
    private List<GymLocation> gymLocations;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gym")
    private List<Membership> memberships;

    public Gym(String name, String description, String email){
        this.name = name;
        this.description = description;
        this.email = email;
    }
}
