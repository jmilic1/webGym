package hr.fer.progi.bugbusters.webgym.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    @Column(unique = true)
    private String username;

    private String email;

    private String password;

    private Boolean client;

    private Boolean coach;

    private Boolean gymOwner;

    public User(Boolean coach, Boolean owner, Boolean client){
        this.coach = coach;
        this.gymOwner = owner;
    }

    public User(String username, String email, String password, String name, String surname){
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        final SimpleGrantedAuthority simpleGrantedAuthority;
        if (coach){
            simpleGrantedAuthority = new SimpleGrantedAuthority("coach");
        } else {
            if (gymOwner){
                simpleGrantedAuthority = new SimpleGrantedAuthority("owner");
            } else {
                simpleGrantedAuthority = new SimpleGrantedAuthority("client");
            }
        }
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
