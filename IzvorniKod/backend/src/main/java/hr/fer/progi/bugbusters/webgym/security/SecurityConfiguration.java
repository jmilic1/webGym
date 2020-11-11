package hr.fer.progi.bugbusters.webgym.security;

import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Autowired
    public SecurityConfiguration(UserService userService){
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("coach")
                .password("asd")
                .roles("coach")
                .and().withUser("client")
                .password("sad")
                .roles("client")
                .and().withUser("owner")
                .password("3124")
                .roles("owner")
                .and().withUser("unregistered")
                .password("noPass")
                .roles("unregistered");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/registration", "/login").permitAll()
                .antMatchers("/gymList").permitAll()
                .antMatchers("/testAuthorization/coach").hasAuthority("coach")
                .antMatchers("/testAuthorization/user").hasAuthority("user")
                .antMatchers("/testAuthorization/owner").hasAuthority("owner")
                .antMatchers("/testAuthorization/unregistered").permitAll();

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(encoder());
    }
}
