package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service with the name "userService" which only serves to create Cities and return them in a list.
 *
 * @author jmilic
 */
@Service("userService")
public class UserService {
    /**
     * Creates the Cities and returns them.
     *
     * @return created cities
     */
    public List<City> createCities() {
        List<City> cities = new ArrayList<City>();

        cities.add(new City(Long.valueOf(5), "Pariz", 2161000));
        cities.add(new City(Long.valueOf(12312), "Austin", 964254));
        cities.add(new City(Long.valueOf(543), "Stockholm", 975904));
        return cities;
    }
}
