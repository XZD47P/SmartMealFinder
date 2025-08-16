package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.DailyProgressSaveReq;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
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
@RequestMapping("/api/progress")
public class DailyProgressController {

    @Autowired
    private DailyProgressService dailyProgressService;

    @Autowired
    private UserService userService;

    @Autowired
    private DietPlanService dietPlanService;

    @PostMapping("/save")
    public ResponseEntity<?> saveProgress(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody DailyProgressSaveReq dailyProgressSaveReq) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);

            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);


            if (dailyProgress == null) {
                this.dailyProgressService.createTodayProgress(user,
                        dietPlan,
                        dailyProgressSaveReq.getWeight(),
                        dailyProgressSaveReq.getCaloriesConsumed(),
                        dailyProgressSaveReq.getProteinConsumed(),
                        dailyProgressSaveReq.getCarbsConsumed(),
                        dailyProgressSaveReq.getFatsConsumed(),
                        dailyProgressSaveReq.getComment());
            } else {
                this.dailyProgressService.updateTodayProgress(dailyProgress,
                        dailyProgressSaveReq.getWeight(),
                        dailyProgressSaveReq.getCaloriesConsumed(),
                        dailyProgressSaveReq.getProteinConsumed(),
                        dailyProgressSaveReq.getCarbsConsumed(),
                        dailyProgressSaveReq.getFatsConsumed(),
                        dailyProgressSaveReq.getComment());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Daily progress successfully saved."));
    }

    @GetMapping
    public ResponseEntity<?> getProgress(@AuthenticationPrincipal UserDetails userDetails) {

        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

            return ResponseEntity.status(HttpStatus.OK).body(dailyProgress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getProgressHistory(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            List<DailyProgress> progressHistoryList = this.dailyProgressService.findAll(user);

            return ResponseEntity.status(HttpStatus.OK).body(progressHistoryList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Something went wrong, please try again later."));
        }
    }
}
