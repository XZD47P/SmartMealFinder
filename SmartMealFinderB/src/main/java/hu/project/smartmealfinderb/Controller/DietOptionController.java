package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.AddUserDietOptionReq;
import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DietOptionService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/diet-option")
public class DietOptionController {

    @Autowired
    private DietOptionService dietOptionService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllDietOptions() {
        try {
            List<DietOption> dietOptions = this.dietOptionService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(dietOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while loading options: " + e.getMessage()));
        }
    }

    @GetMapping("/load-by-user")
    public ResponseEntity<?> getUserDietOptions(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            List<String> userDietOptions = this.dietOptionService.findByUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDietOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while loading user's diet: " + e.getMessage()));
        }
    }

    @PostMapping("/add-to-user")
    public ResponseEntity<?> saveUserDietOption(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody AddUserDietOptionReq addUserDietOptionReq) {

        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            this.dietOptionService.addDietOptionToUser(user, addUserDietOptionReq.getDiets());

            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Diet option added successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while adding diet to user: " + e.getMessage()));
        }

    }
}
