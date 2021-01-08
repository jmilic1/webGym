package hr.fer.progi.bugbusters.webgym.dao;

import hr.fer.progi.bugbusters.webgym.model.Membership;
import hr.fer.progi.bugbusters.webgym.model.MembershipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("membershipUserRep")
public interface MembershipUserRepository extends JpaRepository<MembershipUser, Long> {
}
