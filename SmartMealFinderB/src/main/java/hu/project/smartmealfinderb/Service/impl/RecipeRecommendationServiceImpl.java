package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.RecipeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationServiceImpl implements RecipeRecommendationService {

    private final DietPlanService dietPlanService;
    private final DailyProgressService dailyProgressService;


    @Override
    public RemainingDailyMacrosResp calcRemainingMacros(User user) {
        double calories, protein, carbs, fats;
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
    }
}
