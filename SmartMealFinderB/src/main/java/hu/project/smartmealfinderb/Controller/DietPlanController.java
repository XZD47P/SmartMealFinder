package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.DietPlanRequest;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DietPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plan")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietPlanService dietPlanService;

    @PostMapping("/create")
    public ResponseEntity<?> createDietPlan(@RequestBody DietPlanRequest dietPlanRequest) {

        this.dietPlanService.calculateDietPlan(dietPlanRequest.getSex(),
                dietPlanRequest.getWeight(),
                dietPlanRequest.getHeight(),
                dietPlanRequest.getAge(),
                dietPlanRequest.getActivityLevel(),
                dietPlanRequest.getGoalType(),
                dietPlanRequest.getWeightGoal());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MessageResponse("Diet Plan created successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getDietPlan() {

        DietPlan userDietPlan = this.dietPlanService.getCurrentUserDietPlan();
        return ResponseEntity.status(HttpStatus.OK).body(userDietPlan);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteDietPlan() {

        this.dietPlanService.deleteUserDietPlan();
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Diet Plan deleted successfully"));
    }
}
