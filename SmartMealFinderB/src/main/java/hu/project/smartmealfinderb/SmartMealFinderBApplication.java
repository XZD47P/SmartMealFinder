package hu.project.smartmealfinderb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SmartMealFinderBApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartMealFinderBApplication.class, args);
    }

}
