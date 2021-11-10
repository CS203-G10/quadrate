package cs203t10.quadrate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QuadrateApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuadrateApplication.class, args);
    }

    // @Bean
    // CommandLineRunner commandLineRunner() {

    // }

}
