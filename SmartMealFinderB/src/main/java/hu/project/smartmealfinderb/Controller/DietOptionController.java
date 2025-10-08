package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.AddUserDietOptionReq;
import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DietOptionService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DietOptionController {

    private final DietOptionService dietOptionService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllDietOptions() {

        List<DietOption> dietOptions = this.dietOptionService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(dietOptions);
    }

    @GetMapping("/load-by-user")
    public ResponseEntity<?> getUserDietOptions(@AuthenticationPrincipal UserDetails userDetails) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        List<String> userDietOptions = this.dietOptionService.findByUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userDietOptions);
    }

    @PostMapping("/save-to-user")
    public ResponseEntity<?> saveUserDietOption(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody AddUserDietOptionReq addUserDietOptionReq) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        this.dietOptionService.modifyDietOptionToUser(user, addUserDietOptionReq.getDiets());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Diet option added successfully"));
    }
}
