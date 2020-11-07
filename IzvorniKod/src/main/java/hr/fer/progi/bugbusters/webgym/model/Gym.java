package hr.fer.progi.bugbusters.webgym.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "gyms")
public class Gym {
    private Integer id;
    private String name;
    private String city;

    public Gym(){

    }
    public Gym(String name, String city, int id){
        this.name = name;
        this.city = city;
        this.id = id;
    }

    public Gym(String name, String city){
        this.name = name;
        this.city = city;
    }
    @Id
  //  @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "gym_sequence"),
                    @Parameter(name = "initial_value", value = "4"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){ this.name = name; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) { this.city = city; };
}
