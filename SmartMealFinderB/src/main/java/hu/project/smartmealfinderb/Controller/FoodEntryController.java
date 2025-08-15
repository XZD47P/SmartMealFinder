package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.FoodEntryService;
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
@RequestMapping("/api/food-entry")
public class FoodEntryController {

    @Autowired
    private FoodEntryService foodEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DailyProgressService dailyProgressService;

    @Autowired
    private DietPlanService dietPlanService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFoodEntry(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody SaveFoodEntryReq newFoodEntry) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

            if (dailyProgress != null) {
                this.foodEntryService.addFoodEntry(user,
                        dailyProgress,
                        newFoodEntry.getSpoonacularId(),
                        newFoodEntry.getName(),
                        newFoodEntry.getCalories(),
                        newFoodEntry.getProtein(),
                        newFoodEntry.getCarbs(),
                        newFoodEntry.getFats());
                //TODO: Szétszedni a súlyfelkövetést és a kommentet, összeadást átvinni a servicebe
                this.dailyProgressService.updateTodayProgress(dailyProgress,
                        dailyProgress.getWeight(),
                        (dailyProgress.getCaloriesConsumed() + newFoodEntry.getCalories()),
                        (dailyProgress.getProteinConsumed() + newFoodEntry.getProtein()),
                        (dailyProgress.getCarbsConsumed() + newFoodEntry.getCarbs()),
                        (dailyProgress.getFatsConsumed() + newFoodEntry.getFats()),
                        dailyProgress.getComment());
            } else { //TODO: ÚJRAGONDOLÁS A DAILYPROGRESS MEGSZERZÉSE MIATT
                this.dailyProgressService.createTodayProgress(user,
                        dietPlan,
                        0,
                        newFoodEntry.getCalories(),
                        newFoodEntry.getProtein(),
                        newFoodEntry.getCarbs(),
                        newFoodEntry.getFats(),
                        "new");

                dailyProgress = this.dailyProgressService.findTodayProgress(user);
                this.foodEntryService.addFoodEntry(user,
                        dailyProgress,
                        newFoodEntry.getSpoonacularId(),
                        newFoodEntry.getName(),
                        newFoodEntry.getCalories(),
                        newFoodEntry.getProtein(),
                        newFoodEntry.getCarbs(),
                        newFoodEntry.getFats());
            }

            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Saved Successfully"));
        } catch (Exception e) {
            System.err.println("There was an error saving your food entry: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error saving food intake"));
        }

    }
}
