package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/public/auth/signup")
    public ResponseEntity<?> registerUser(@Valid
                                          @RequestParam String email,
                                          @RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam Set<String> role
    ) {

        try {
            this.userService.registerUser(email, username, password, role);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("User registered successfully");
    }
}
