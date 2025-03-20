package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Security.Request.LoginRequest;
import hu.project.smartmealfinderb.Security.Request.RegisterRequest;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
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
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/public/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        LoginResponse response = this.userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(response);
    }
}
