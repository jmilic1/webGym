package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.JobRequest;
import hr.fer.progi.bugbusters.webgym.model.MembershipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("jobRequestRep")
public interface JobRequestRepository extends JpaRepository<JobRequest, Long> {
}
