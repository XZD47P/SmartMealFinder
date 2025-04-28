package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Request.DailyProgressPostReq;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<?> postProgress(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestBody DailyProgressPostReq dailyProgressPostReq) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);

            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);


            if (dailyProgress == null) {
                this.dailyProgressService.createTodayProgress(user,
                        dietPlan,
                        dailyProgressPostReq.getWeight(),
                        dailyProgressPostReq.getCaloriesConsumed(),
                        dailyProgressPostReq.getProteinConsumed(),
                        dailyProgressPostReq.getCarbsConsumed(),
                        dailyProgressPostReq.getFatsConsumed(),
                        dailyProgressPostReq.getComment());
            } else {
                this.dailyProgressService.updateTodayProgress(dailyProgress,
                        dailyProgressPostReq.getWeight(),
                        dailyProgressPostReq.getCaloriesConsumed(),
                        dailyProgressPostReq.getProteinConsumed(),
                        dailyProgressPostReq.getCarbsConsumed(),
                        dailyProgressPostReq.getFatsConsumed(),
                        dailyProgressPostReq.getComment());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Daily progress successfully saved."));
    }
}
