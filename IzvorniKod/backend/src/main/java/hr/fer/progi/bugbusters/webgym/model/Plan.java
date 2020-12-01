package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne//(cascade = CascadeType.PERSIST)
    @JoinColumn(name="username")//, referencedColumnName = "username")
    private User user;

    private String description;
    private Date dateBegin;
    private Date dateEnd;
    private Double price;
    private Boolean isWorkout;

    public Plan(User user, String description){
        this.user = user;
        this.description = description;
    }
}
