package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("planRep")
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUser(User user);
}
