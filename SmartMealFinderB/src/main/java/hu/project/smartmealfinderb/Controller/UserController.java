package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.ChangePasswordRequest;
import hu.project.smartmealfinderb.DTO.Request.ResetPasswordChangeReq;
import hu.project.smartmealfinderb.DTO.UserInfoResponse;
import hu.project.smartmealfinderb.Security.Request.LoginRequest;
import hu.project.smartmealfinderb.Security.Request.RegisterRequest;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@Valid
                                          @RequestBody RegisterRequest registerRequest) {

        this.userService.registerUser(registerRequest.getEmail(),
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getRole(),
                registerRequest.getFirstName(),
                registerRequest.getLastName());

        this.userService.generateVerificationToken(registerRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {

        LoginResponse response = this.userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {

        UserInfoResponse userInfo = this.userService.getUserInfo(userDetails);
        return ResponseEntity.ok().body(userInfo);
    }

    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        this.userService.generatePasswordResetToken(email);
        return ResponseEntity.ok(new MessageResponse("Password reset token generated successfully and sent by email!"));
    }

    @PostMapping("/public/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordChangeReq resetPasswordChangeReq) {

        this.userService.resetPassword(resetPasswordChangeReq.getToken(), resetPasswordChangeReq.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset successful"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {

        this.userService.changePassword(request.getToken(),
                request.getOldPassword(),
                request.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }

    @PostMapping("/public/verify-user")
    public ResponseEntity<?> verifyUser(@RequestParam String token) {

        this.userService.verifyUser(token);
        return ResponseEntity.ok(new MessageResponse("User verified successfully"));
    }

}
