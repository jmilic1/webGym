package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Gym;
import hr.fer.progi.bugbusters.webgym.model.GymUser;
import hr.fer.progi.bugbusters.webgym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gymUserRep")
public interface GymUserRepository extends JpaRepository<GymUser, Long> {
    List<GymUser> findByUser(User user);
    List<GymUser> findByGym(Gym gym);
}
