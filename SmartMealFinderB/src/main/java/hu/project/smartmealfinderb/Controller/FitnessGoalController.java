package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Service.FitnessGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/fitness-goal")
@RequiredArgsConstructor
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDietGoals() {

        List<FitnessGoal> fitnessGoals = this.fitnessGoalService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(fitnessGoals);
    }
}
