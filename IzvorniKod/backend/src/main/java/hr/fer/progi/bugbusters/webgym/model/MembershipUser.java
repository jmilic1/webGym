package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MembershipUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="username")
    private User user;
    @ManyToOne
    private Membership membership;
    private Date dateBegin;
    private Date dateEnd;

    @Override
    public String toString(){
        return "id: " + id + ", user.username: " + user.getUsername() + ", membership.id: " + membership.getId() + ", dateBegin: " + dateBegin
                + ", dateEnd: " + dateEnd;
    }
}
