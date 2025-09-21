package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/intolerance")
public class IntoleranceController {

    @Autowired
    private IntoleranceService intoleranceService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllIntolerances() {
        try {
            List<Intolerance> intolerances = this.intoleranceService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(intolerances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while listing the intolerances: " + e.getMessage()));
        }
    }

    @GetMapping("/load-by-user")
    public ResponseEntity<?> getUserIntolerances(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            List<String> userIntolerances = this.intoleranceService.findByUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(userIntolerances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while loading user's diet: " + e.getMessage()));
        }
    }
}
