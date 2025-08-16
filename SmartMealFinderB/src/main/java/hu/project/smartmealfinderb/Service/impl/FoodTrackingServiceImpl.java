package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodTrackingServiceImpl implements FoodTrackingService {

    @Autowired
    private FoodEntryService foodEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private DailyProgressService dailyProgressService;

    @Autowired
    private DietPlanService dietPlanService;

    @Override
    public void saveFoodEntry(String username, SaveFoodEntryReq newFoodEntry) {

        User user = this.userService.findByUsername(username);
        DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
        DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

        //TODO: Szétszedni a súlyfelkövetést és a kommentet, összeadást átvinni a servicebe
        if (dailyProgress == null) {
            this.dailyProgressService.createTodayProgress(user,
                    dietPlan,
                    0,
                    newFoodEntry.getCalories(),
                    newFoodEntry.getProtein(),
                    newFoodEntry.getCarbs(),
                    newFoodEntry.getFats(),
                    "new");
        } else {
            this.dailyProgressService.updateTodayProgress(dailyProgress,
                    dailyProgress.getWeight(),
                    newFoodEntry.getCalories(),
                    newFoodEntry.getProtein(),
                    newFoodEntry.getCarbs(),
                    newFoodEntry.getFats(),
                    dailyProgress.getComment());
        }

        this.foodEntryService.addFoodEntry(user,
                dailyProgress,
                newFoodEntry.getSpoonacularId(),
                newFoodEntry.getName(),
                newFoodEntry.getCalories(),
                newFoodEntry.getProtein(),
                newFoodEntry.getCarbs(),
                newFoodEntry.getFats());
    }
}
