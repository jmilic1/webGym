package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("gymRep")
public interface GymRepository extends JpaRepository<Gym, Long> {
}
