package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.WeeklyPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/load")
    public ResponseEntity<?> getWeeklyPlan(@RequestParam int year,
                                           @RequestParam int week) {
        WeeklyMealPlanDTO plan = this.weeklyPlannerService.getWeeklyMealPlan(year, week);
        return ResponseEntity.status(HttpStatus.OK).body(plan);
    }
}
