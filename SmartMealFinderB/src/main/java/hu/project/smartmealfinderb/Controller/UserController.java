package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Security.Request.RegisterRequest;
import hu.project.smartmealfinderb.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/public/auth/register")
    public ResponseEntity<?> registerUser(@Valid
                                          @RequestBody RegisterRequest registerRequest
    ) {

        try {
            this.userService.registerUser(registerRequest.getEmail(),
                    registerRequest.getUsername(),
                    registerRequest.getPassword(),
                    registerRequest.getRole());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("User registered successfully");
    }
}
