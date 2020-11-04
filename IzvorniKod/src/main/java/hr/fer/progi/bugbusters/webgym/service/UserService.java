package hr.fer.progi.bugbusters.webgym.service;

import hr.fer.progi.bugbusters.webgym.dao.GymRepository;
import hr.fer.progi.bugbusters.webgym.model.Gym;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final GymRepository gymRepository;

    @Autowired
    public UserService(@Qualifier("gymRep") GymRepository gymRepository){
        this.gymRepository = gymRepository;
    }

    /**
     * Creates the Gyms and returns them.
     *
     * @return created gyms
     */
    /*
    public List<Gym> createGyms() {
        List<Gym> gyms = new ArrayList<>();
        gyms.add(new Gym(Long.valueOf(5), "American Top Team", "Zagreb"));
        gyms.add(new Gym(Long.valueOf(12312), "CrossFit", "Osijek"));
        gyms.add(new Gym(Long.valueOf(543), "Gyms4You", "Split"));
        return gyms;
    }*/

    public List<Gym> databaseGyms(){
        Iterable<Gym> it = gymRepository.findAll();
        List<Gym> gyms = new ArrayList<>();

        for (Gym gym:it){
            gyms.add(gym);
        }
        return gyms;
    }

    public boolean addGym(Gym gym){
       /* try {
        if (gymRepository.existsById(gym.getId())){
            return false;
        }}
        catch (IllegalArgumentException ex){

        }*/
       /* Gym newGym = new Gym(gym.getName(), gym.getCity(), gym.getId());
        newGym.setId(796);
        System.out.println("THE GYM IS\n OF NAME\n " + newGym.getName() + "City: " + newGym.getCity() + "and id: " + newGym.getId());
       */ gymRepository.save(gym);
        return true;
    }
}
