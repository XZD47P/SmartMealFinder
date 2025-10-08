package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import hu.project.smartmealfinderb.Service.FoodTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FoodTrackingServiceImpl implements FoodTrackingService {

    private final FoodEntryService foodEntryService;
    private final DailyProgressService dailyProgressService;
    private final DietPlanService dietPlanService;

    @Override
    public void saveFoodEntry(User user, SaveFoodEntryReq newFoodEntry) {
        try {
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

            if (dailyProgress == null) {
                this.dailyProgressService.createTodayProgress(user,
                        dietPlan,
                        newFoodEntry.getCalories(),
                        newFoodEntry.getProtein(),
                        newFoodEntry.getCarbs(),
                        newFoodEntry.getFats());
                dailyProgress = this.dailyProgressService.findTodayProgress(user);
            } else {
                this.dailyProgressService.updateTodayProgress(dailyProgress,
                        dailyProgress.getCaloriesConsumed() + newFoodEntry.getCalories(),
                        dailyProgress.getProteinConsumed() + newFoodEntry.getProtein(),
                        dailyProgress.getCarbsConsumed() + newFoodEntry.getCarbs(),
                        dailyProgress.getFatsConsumed() + newFoodEntry.getFats());
            }

            this.foodEntryService.addFoodEntry(user,
                    dailyProgress,
                    newFoodEntry.getSpoonacularId(),
                    newFoodEntry.getName(),
                    newFoodEntry.getCalories(),
                    newFoodEntry.getProtein(),
                    newFoodEntry.getCarbs(),
                    newFoodEntry.getFats());
        } catch (Exception e) {
            throw new RuntimeException("There was an error while saving food entry: " + e.getMessage());
        }
    }

    @Override
    public void deleteFoodEntry(User user, Long foodEntryId) {
        try {
            FoodEntry foodEntry = this.foodEntryService.findById(foodEntryId);

            if (!foodEntry.getUser().equals(user)) {
                throw new RuntimeException("The entry is not owned by the user");
            }

            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);
            this.dailyProgressService.updateTodayProgress(dailyProgress,
                    dailyProgress.getCaloriesConsumed() - foodEntry.getCalories(),
                    dailyProgress.getProteinConsumed() - foodEntry.getProtein(),
                    dailyProgress.getCarbsConsumed() - foodEntry.getCarbs(),
                    dailyProgress.getFatsConsumed() - foodEntry.getFats());

            this.foodEntryService.deleteById(foodEntryId);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while deleting food entry: " + e.getMessage());
        }
    }


}
