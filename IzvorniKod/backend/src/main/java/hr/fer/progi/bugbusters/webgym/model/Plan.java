package hr.fer.progi.bugbusters.webgym.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="username")
    private User user;
    private String description;
    private Date dateFrom;
    private Date dateTo;
    private Double price;
    private Boolean isTraining;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plan")
    private List<PlanClient> clientPlans;

    public Plan(User user, String description){
        this.user = user;
        this.description = description;
    }
}
