package hr.fer.progi.bugbusters.webgym.model;

/**
 * A class which models a City.
 *
 * @author jmilic
 */
public class City {
    private Long id;
    private String name;
    private Integer population;

    public City(Long id, String name, Integer population) {
        this.id = id;
        this.name = name;
        this.population = population;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPopulation() {
        return population;
    }
}
