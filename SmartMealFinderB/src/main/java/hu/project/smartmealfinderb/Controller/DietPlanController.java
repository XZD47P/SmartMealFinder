package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Request.DietPlanRequest;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plan")
public class DietPlanController {

    @Autowired
    private DietPlanService dietPlanService;

    @PostMapping("/create")
    public ResponseEntity<?> createDietPlan(@RequestBody DietPlanRequest dietPlanRequest) {
        double response;

        try {
            response = this.dietPlanService.calculateDietPlan(dietPlanRequest.getSex(),
                    dietPlanRequest.getWeight(),
                    dietPlanRequest.getHeight(),
                    dietPlanRequest.getAge(),
                    dietPlanRequest.getActivityLevel(),
                    dietPlanRequest.getGoalType(),
                    dietPlanRequest.getWeightGoal(),
                    dietPlanRequest.getDaysToReachGoal()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
