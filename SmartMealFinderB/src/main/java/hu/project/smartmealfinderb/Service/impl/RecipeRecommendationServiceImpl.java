package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.RecipeRecommendationService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationServiceImpl implements RecipeRecommendationService {

    private final DietPlanService dietPlanService;
    private final DailyProgressService dailyProgressService;
    private final UserService userService;


    @Override
    public RemainingDailyMacrosResp calcRemainingMacros() {
        try {
            double calories, protein, carbs, fats;
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("user is null");
            }
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);

            if (dailyProgress == null) {
                calories = dietPlan.getGoalCalorie();
                protein = dietPlan.getGoalProtein();
                carbs = dietPlan.getGoalCarbohydrate();
                fats = dietPlan.getGoalFat();
            } else {
                calories = dietPlan.getGoalCalorie() - dailyProgress.getCaloriesConsumed();
                protein = dietPlan.getGoalProtein() - dailyProgress.getProteinConsumed();
                carbs = dietPlan.getGoalCarbohydrate() - dailyProgress.getCarbsConsumed();
                fats = dietPlan.getGoalFat() - dailyProgress.getFatsConsumed();
            }


            List<String> diets = user.getDietOptions().stream()
                    .map(userDietOption -> userDietOption.getDietOption().getApiValue())
                    .toList();

            List<String> intolerances = user.getIntolerances().stream()
                    .map(userIntolerance -> userIntolerance.getIntolerance().getApiValue())
                    .toList();

            return new RemainingDailyMacrosResp(calories, protein, carbs, fats, diets, intolerances);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while calculating the remaining macros: " + e.getMessage(), e);
        }
    }
}
