package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.DietPlanRequest;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
public class DietPlanController {

    @Autowired
    private DietPlanService dietPlanService;

    @Autowired
    private UserService userService;

    @Autowired
    private DailyProgressService dailyProgressService;

    @Autowired
    private FoodEntryService foodEntryService;

    @PostMapping("/create")
    public ResponseEntity<?> createDietPlan(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody DietPlanRequest dietPlanRequest) {


        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            this.dietPlanService.calculateDietPlan(user,
                    dietPlanRequest.getSex(),
                    dietPlanRequest.getWeight(),
                    dietPlanRequest.getHeight(),
                    dietPlanRequest.getAge(),
                    dietPlanRequest.getActivityLevel(),
                    dietPlanRequest.getGoalType(),
                    dietPlanRequest.getWeightGoal());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Could not save diet plan"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse("Diet Plan created successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getDietPlan(@AuthenticationPrincipal UserDetails userDetails) {

        DietPlan userDietPlan;

        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            userDietPlan = this.dietPlanService.getUserDietPlan(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(userDietPlan);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteDietPlan(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            this.foodEntryService.deleteAllUserFoodEntries(user);
            this.dailyProgressService.deleteUserProgression(user);
            this.dietPlanService.deleteUserDietPlan(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Diet Plan deleted successfully"));
    }
}
