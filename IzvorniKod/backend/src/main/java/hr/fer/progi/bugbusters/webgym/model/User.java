package hr.fer.progi.bugbusters.webgym.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Plan> plans;

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
}
