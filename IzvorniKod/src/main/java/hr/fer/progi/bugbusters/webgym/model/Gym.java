package hr.fer.progi.bugbusters.webgym.model;

import javax.persistence.*;

@Entity
@Table(name = "gyms")
public class Gym {
    private Integer id;
    private String name;
    private String city;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
