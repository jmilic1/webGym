package hr.fer.progi.bugbusters.webgym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Demo application for backend of webgym project.
 * Application can recieve a GET request to return hardcoded instances of City models to user.
 *
 * This demo does not reflect the specifications of the project,
 * but instead only serves as a demo for connecting with a frontend service.
 *
 * @author jmilic
 */
@SpringBootApplication
public class WebgymApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebgymApplication.class, args);
    }

}
