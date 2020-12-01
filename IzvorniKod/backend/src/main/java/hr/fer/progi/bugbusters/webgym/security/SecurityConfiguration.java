package hr.fer.progi.bugbusters.webgym.security;

import hr.fer.progi.bugbusters.webgym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                .roles("COACH")
                .and().withUser("client")
                .password("sad")
                .roles("CLIENT")
                .and().withUser("owner")
                .password("3124")
                .roles("OWNER")
                .and().withUser("unregistered")
                .password("noPass")
                .roles("unregistered");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/registration", "/login").permitAll()
                .antMatchers("/gymList").permitAll()
                .antMatchers("/testAuthorization/coach").hasAuthority("COACH")
                .antMatchers("/testAuthorization/user").hasAuthority("USER")
                .antMatchers("/testAuthorization/owner").hasAuthority("OWNER")
                .antMatchers("/testAuthorization/unregistered").permitAll();

        http.csrf().disable();
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(encoder());
    }
}
