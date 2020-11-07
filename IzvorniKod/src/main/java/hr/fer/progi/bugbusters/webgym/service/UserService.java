package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service with the name "userService" which only serves to create Gyms and return them in a list.
 *
 * @author jmilic
 */
@Service("userService")
public class UserService {
    /**
     * Creates the Gyms and returns them.
     *
     * @return created gyms
     */
    public List<Gym> createGyms() {
        List<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym(Long.valueOf(5), "American Top Team", "Zagreb"));
        gyms.add(new Gym(Long.valueOf(12312), "CrossFit", "Osijek"));
        gyms.add(new Gym(Long.valueOf(543), "Gyms4You", "Split"));
        return gyms;
    }
}
