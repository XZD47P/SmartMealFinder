package hu.project.smartmealfinderb.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/public/hello")
    public String hello() {
        return "Welcome to Smart Meal Finder!";
    }
}
