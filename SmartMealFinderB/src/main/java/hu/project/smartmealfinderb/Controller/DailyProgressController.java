package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.WeightLogReq;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
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
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class DailyProgressController {

    private final DailyProgressService dailyProgressService;
    private final UserService userService;
    private final DietPlanService dietPlanService;

    @PostMapping("/weight/save")
    public ResponseEntity<?> saveWeightProgress(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody WeightLogReq weightLogReq) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
        this.dailyProgressService.saveWeight(user,
                dietPlan,
                weightLogReq.getWeight(),
                weightLogReq.getComment());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Daily progress successfully saved."));
    }

    @GetMapping
    public ResponseEntity<?> getProgress(@AuthenticationPrincipal UserDetails userDetails) {


        User user = this.userService.findByUsername(userDetails.getUsername());
        DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

        return ResponseEntity.status(HttpStatus.OK).body(dailyProgress);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProgressHistory(@AuthenticationPrincipal UserDetails userDetails) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        List<DailyProgress> progressHistoryList = this.dailyProgressService.findAll(user);

        return ResponseEntity.status(HttpStatus.OK).body(progressHistoryList);
    }
}
