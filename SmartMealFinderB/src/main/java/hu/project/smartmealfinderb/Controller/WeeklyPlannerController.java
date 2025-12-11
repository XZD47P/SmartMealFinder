package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.WeeklyPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weekly-planner")
@RequiredArgsConstructor
public class WeeklyPlannerController {

    private final WeeklyPlannerService weeklyPlannerService;

    @PostMapping("/save")
    public ResponseEntity<?> savePlan(@RequestBody WeeklyMealPlanDTO weeklyMealPlanDTO) {
        this.weeklyPlannerService.saveWeeklyMealPlan(weeklyMealPlanDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Successfully saved meal plan"));
    }
}
