package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.DietGoal;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DietGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/dietgoal")
public class DietGoalController {

    @Autowired
    private DietGoalService dietGoalService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDietGoals() {
        try {
            List<DietGoal> dietGoals = this.dietGoalService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(dietGoals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }
    }
}
