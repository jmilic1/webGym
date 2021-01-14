package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.postgresql.util.PGInterval;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Gym gym;
    private Double price;
    private String description;
    private String interval;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "membership")
    private List<MembershipUser> membershipUserList;

    @Override
    public String toString(){
        return "id: " + id + ", gym.id: " + gym.getId() + ", price: " + price + ", description: " + description
                + ", interval: " + interval;
    }
}
