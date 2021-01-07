package hr.fer.progi.bugbusters.webgym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Demo application for backend of webgym project.
 *
 *
 * This demo does not reflect the specifications of the project,
 * but instead only serves as a demo for connecting with a frontend service,
 * listing all of the gyms in remote database,
 * and providing basic register/login/logout functionality.
 */
@SpringBootApplication
public class WebgymApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebgymApplication.class, args);
    }

}
