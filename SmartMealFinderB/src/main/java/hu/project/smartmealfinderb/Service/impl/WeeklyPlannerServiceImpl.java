package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.RecipeTileDTO;
import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;
import hu.project.smartmealfinderb.Repository.WeeklyMealPlanRepository;
import hu.project.smartmealfinderb.Service.UserService;
import hu.project.smartmealfinderb.Service.WeeklyPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyPlannerServiceImpl implements WeeklyPlannerService {

    private final WeeklyMealPlanRepository weeklyMealPlanRepository;
    private final UserService userService;

    @Override
    public void saveWeeklyMealPlan(WeeklyMealPlanDTO weeklyMealPlanDTO) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();

            this.weeklyMealPlanRepository.deleteByUserAndPlanningYearAfterAndWeekNumber(user,
                    weeklyMealPlanDTO.getYear(),
                    weeklyMealPlanDTO.getWeekNumber());

            Map<String, List<RecipeTileDTO>> weeklyPlanMap = weeklyMealPlanDTO.getPlan();

            if (weeklyPlanMap == null || weeklyPlanMap.isEmpty()) {
                return;
            }

            List<WeeklyMealPlan> mealsToSave = new ArrayList<>();

            for (Map.Entry<String, List<RecipeTileDTO>> entry : weeklyPlanMap.entrySet()) {
                String dayOfWeek = entry.getKey();
                List<RecipeTileDTO> recipes = entry.getValue();

                if (recipes == null || recipes.isEmpty()) {
                    continue;
                }

                for (RecipeTileDTO recipe : recipes) {
                    double calories = this.getNutrientAmount(recipe, "Calories");
                    double protein = this.getNutrientAmount(recipe, "Protein");
                    double carbs = this.getNutrientAmount(recipe, "Carbohydrates");
                    double fat = this.getNutrientAmount(recipe, "Fat");

                    WeeklyMealPlan meal = new WeeklyMealPlan();
                    meal.setUser(user);
                    meal.setPlanningYear(weeklyMealPlanDTO.getYear());
                    meal.setWeekNumber(weeklyMealPlanDTO.getWeekNumber());
                    meal.setDayOfWeek(dayOfWeek);
                    meal.setRecipeId(recipe.getId());
                    meal.setTitle(recipe.getTitle());
                    meal.setImage(recipe.getImage());
                    meal.setCalories(calories);
                    meal.setProtein(protein);
                    meal.setCarbs(carbs);
                    meal.setFat(fat);

                    mealsToSave.add(meal);
                }
            }

            if (!mealsToSave.isEmpty()) {
                this.weeklyMealPlanRepository.saveAll(mealsToSave);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving weekly meal plan: " + e.getMessage(), e);
        }
    }

    private double getNutrientAmount(RecipeTileDTO recipe, String nutrientName) {
        if (recipe.getNutrition() == null) {
            return 0.0;
        } else {
            return recipe.getNutrition().getNutrients().stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase(nutrientName))
                    .findFirst()
                    .map(RecipeTileDTO.Nutrient::getAmount)
                    .orElse(0.0);
        }
    }
}
