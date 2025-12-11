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
import java.util.HashMap;
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

    @Override
    public WeeklyMealPlanDTO getWeeklyMealPlan(int year, int week) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();

            List<WeeklyMealPlan> meals = this.weeklyMealPlanRepository.findByUserAndPlanningYearAndWeekNumber(user, year, week);

            Map<String, List<RecipeTileDTO>> planMap = new HashMap<>();
            List<String> days = List.of("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
            days.forEach(day -> planMap.put(day, new ArrayList<>()));

            for (WeeklyMealPlan meal : meals) {
                RecipeTileDTO dto = new RecipeTileDTO();
                dto.setId(meal.getRecipeId());
                dto.setTitle(meal.getTitle());
                dto.setImage(meal.getImage());


                RecipeTileDTO.Nutrition nutrition = new RecipeTileDTO.Nutrition();
                List<RecipeTileDTO.Nutrient> nutrientList = new ArrayList<>();

                nutrientList.add(createNutrient("Calories", meal.getCalories(), "kcal"));
                nutrientList.add(createNutrient("Protein", meal.getProtein(), "g"));
                nutrientList.add(createNutrient("Carbohydrates", meal.getCarbs(), "g"));
                nutrientList.add(createNutrient("Fat", meal.getFat(), "g"));

                nutrition.setNutrients(nutrientList);
                dto.setNutrition(nutrition);


                String day = meal.getDayOfWeek().toLowerCase();
                if (planMap.containsKey(day)) {
                    planMap.get(day).add(dto);
                }

            }

            WeeklyMealPlanDTO response = new WeeklyMealPlanDTO();
            response.setYear(year);
            response.setWeekNumber(week);
            response.setPlan(planMap);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error getting weekly meal plan: " + e.getMessage(), e);
        }
    }

    private RecipeTileDTO.Nutrient createNutrient(String name, double amount, String unit) {
        RecipeTileDTO.Nutrient n = new RecipeTileDTO.Nutrient();
        n.setName(name);
        n.setAmount(amount);
        n.setUnit(unit);
        return n;
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
