package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Plan;
import hr.fer.progi.bugbusters.webgym.model.PlanClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("planClientRep")
public interface PlanClientRepository extends JpaRepository<PlanClient, Long> {
    List<PlanClient> findByPlan(Plan plan);
}
