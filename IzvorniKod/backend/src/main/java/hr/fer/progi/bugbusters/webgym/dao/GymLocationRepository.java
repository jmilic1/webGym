package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.model.GymLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gymLocationRep")
public interface GymLocationRepository extends JpaRepository<GymLocation, Long> {
    List<GymLocation> findByGym(Gym gym);
}
