package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "username")
public class User implements UserDetails {
    @Id
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String PayPalAccount;
    private Integer height;
    private Integer weight;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Plan> plans;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Goal> goals;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<MembershipUser> membershipUserList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<JobRequest> jobRequests;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<PlanClient> clientPlans;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<GymUser> GymUserList;

    public User(String username, String email, String password, String name, String surname){
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority;
        simpleGrantedAuthority = new SimpleGrantedAuthority(getRole().toString());

        return Collections.singletonList(simpleGrantedAuthority);
    }

    public void addPlanClient(PlanClient planClient) {
        if (clientPlans == null) clientPlans = new ArrayList<>();
        clientPlans.add(planClient);
    }

    public void addGoal(Goal goal) {
        if (goals == null) goals = new ArrayList<>();
        goals.add(goal);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString(){
        return "username: " + username;
    }
}
