package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class JobRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="username")
    private User user;
    @ManyToOne
    private Gym gym;
    private String description;
    private JobRequestState state;

    @Override
    public String toString(){
        return "id: " + id + ", gym.id: " + gym.getId() + ", user.username: " + user.getUsername() + ", description: " + description
                + ", state: " + state;
    }
}
