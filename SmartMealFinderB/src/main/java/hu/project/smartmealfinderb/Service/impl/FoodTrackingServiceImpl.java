package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.MacroTotals;
import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FoodTrackingServiceImpl implements FoodTrackingService {

    private final FoodEntryService foodEntryService;
    private final DailyProgressService dailyProgressService;
    private final DietPlanService dietPlanService;
    private final UserService userService;

    @Override
    public void saveFoodEntry(SaveFoodEntryReq newFoodEntry) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
            DailyProgress dailyProgress = this.dailyProgressService.findTodayProgress(user);
            MacroTotals macronutrientTotals;

            switch (newFoodEntry.getCategory()) {
                case "product":
                    macronutrientTotals = this.saveProductEntry(newFoodEntry);
                    break;
                case "ingredient":
                    macronutrientTotals = this.saveIngredientEntry(newFoodEntry);
                    break;
                case "recipe":
                    macronutrientTotals = this.saveRecipeEntry(newFoodEntry);
                    break;
                default:
                    throw new RuntimeException("Invalid food entry category");
            }

            if (dailyProgress == null) {
                this.dailyProgressService.createTodayProgress(user,
                        dietPlan,
                        macronutrientTotals.getCalories(),
                        macronutrientTotals.getProtein(),
                        macronutrientTotals.getCarbs(),
                        macronutrientTotals.getFats());
                dailyProgress = this.dailyProgressService.findTodayProgress(user);
            } else {
                this.dailyProgressService.updateTodayProgress(dailyProgress,
                        dailyProgress.getCaloriesConsumed() + macronutrientTotals.getCalories(),
                        dailyProgress.getProteinConsumed() + macronutrientTotals.getProtein(),
                        dailyProgress.getCarbsConsumed() + macronutrientTotals.getCarbs(),
                        dailyProgress.getFatsConsumed() + macronutrientTotals.getFats());
            }

            this.foodEntryService.addFoodEntry(user,
                    dailyProgress,
                    newFoodEntry.getSpoonacularId(),
                    newFoodEntry.getName(),
                    newFoodEntry.getCategory(),
                    newFoodEntry.getQuantity(),
                    newFoodEntry.getUnit(),
                    macronutrientTotals.getCalories(),
                    macronutrientTotals.getProtein(),
                    macronutrientTotals.getCarbs(),
                    macronutrientTotals.getFats());
        } catch (Exception e) {
            throw new RuntimeException("There was an error while saving food entry: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFoodEntry(Long foodEntryId) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
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
            throw new RuntimeException("There was an error while deleting food entry: " + e.getMessage(), e);
        }
    }

    private MacroTotals saveProductEntry(SaveFoodEntryReq newFoodEntry) {
        double calories, protein, carbs, fats;

        calories = newFoodEntry.getCalories() * newFoodEntry.getQuantity();
        protein = newFoodEntry.getProtein() * newFoodEntry.getQuantity();
        carbs = newFoodEntry.getCarbs() * newFoodEntry.getQuantity();
        fats = newFoodEntry.getFats() * newFoodEntry.getQuantity();

        return new MacroTotals(this.roundUp(calories),
                this.roundUp(protein),
                this.roundUp(carbs),
                this.roundUp(fats));
    }

    private MacroTotals saveIngredientEntry(SaveFoodEntryReq newFoodEntry) {
        double calories, protein, carbs, fats;

        calories = newFoodEntry.getCalories();
        protein = newFoodEntry.getProtein();
        carbs = newFoodEntry.getCarbs();
        fats = newFoodEntry.getFats();

        return new MacroTotals(this.roundUp(calories),
                this.roundUp(protein),
                this.roundUp(carbs),
                this.roundUp(fats));
    }

    private MacroTotals saveRecipeEntry(SaveFoodEntryReq newFoodEntry) {
        double calories, protein, carbs, fats;
        switch (newFoodEntry.getUnit()) {
            case "serving":
                calories = newFoodEntry.getCalories() * newFoodEntry.getQuantity();
                protein = newFoodEntry.getProtein() * newFoodEntry.getQuantity();
                carbs = newFoodEntry.getCarbs() * newFoodEntry.getQuantity();
                fats = newFoodEntry.getFats() * newFoodEntry.getQuantity();
                break;
            case "grams":
                if (newFoodEntry.getWeightPerServing() == 0) {
                    throw new RuntimeException("Cannot use grams as a Unit, because API doesn't provide grams for recipe");
                } else {
                    calories = (newFoodEntry.getCalories() / newFoodEntry.getWeightPerServing()) * newFoodEntry.getQuantity();
                    protein = (newFoodEntry.getProtein() / newFoodEntry.getWeightPerServing()) * newFoodEntry.getQuantity();
                    carbs = (newFoodEntry.getCarbs() / newFoodEntry.getWeightPerServing()) * newFoodEntry.getQuantity();
                    fats = (newFoodEntry.getFats() / newFoodEntry.getWeightPerServing()) * newFoodEntry.getQuantity();
                }
                break;
            default:
                throw new RuntimeException("Unsupported unit: " + newFoodEntry.getUnit());
        }

        return new MacroTotals(this.roundUp(calories),
                this.roundUp(protein),
                this.roundUp(carbs),
                this.roundUp(fats));
    }

    private double roundUp(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }

}
