package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.PlanClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("planClientRep")
public interface PlanClientRepository extends JpaRepository<PlanClient, Long> {
}
