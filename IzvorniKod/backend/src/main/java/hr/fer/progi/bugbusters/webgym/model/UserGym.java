package hr.fer.progi.bugbusters.webgym.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "userGyms")
@NoArgsConstructor
public class UserGym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private GymLocation gym;
    @ManyToOne
    @JoinColumn(name="username")
    private User user;
    private Date workDateBegin;
}
