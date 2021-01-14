package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="username")
    private User user;
    private String description;
    private Double percentage = (double) 0;

     public Goal(User user, String description, Double percentCompleted) {
         this.user = user;
         this.description = description;
         this.percentage = percentCompleted;
     }

     @Override
     public String toString(){
         return "id: " + id + ", User.username: " + user.getUsername() + ", description: " + description + ", percentCompleted: " + percentage;
     }
}
