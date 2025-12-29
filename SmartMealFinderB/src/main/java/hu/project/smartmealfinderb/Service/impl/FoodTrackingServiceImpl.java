package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.MacroTotals;
import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.DTO.Response.IngredientInfo;
import hu.project.smartmealfinderb.DTO.Response.ProductInfo;
import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.*;
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
    private final FoodApiService foodApiService;
    private final RecommendationService recommendationService;

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

            switch (newFoodEntry.getType()) {
                case "product":
                    macronutrientTotals = this.saveProductEntry(newFoodEntry);
                    break;
                case "ingredient":
                    macronutrientTotals = this.saveIngredientEntry(newFoodEntry);
                    break;
                case "recipe":
                    macronutrientTotals = this.saveRecipeEntry(newFoodEntry, user);
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
                        this.roundUp(dailyProgress.getCaloriesConsumed() + macronutrientTotals.getCalories()),
                        this.roundUp(dailyProgress.getProteinConsumed() + macronutrientTotals.getProtein()),
                        this.roundUp(dailyProgress.getCarbsConsumed() + macronutrientTotals.getCarbs()),
                        this.roundUp(dailyProgress.getFatsConsumed() + macronutrientTotals.getFats()));
            }

            this.foodEntryService.addFoodEntry(user,
                    dailyProgress,
                    newFoodEntry.getId(),
                    newFoodEntry.getName(),
                    newFoodEntry.getType(),
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
                    this.roundUp(dailyProgress.getCaloriesConsumed() - foodEntry.getCalories()),
                    this.roundUp(dailyProgress.getProteinConsumed() - foodEntry.getProtein()),
                    this.roundUp(dailyProgress.getCarbsConsumed() - foodEntry.getCarbs()),
                    this.roundUp(dailyProgress.getFatsConsumed() - foodEntry.getFats()));

            if (foodEntry.getCategory().equals("recipe") && user.isRecommendationEnabled()) {
                this.recommendationService.deleteInteractionFromGorse(Interaction.ATE, user, foodEntry.getSpoonacularId());
            }

            this.foodEntryService.deleteById(foodEntryId);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while deleting food entry: " + e.getMessage(), e);
        }
    }

    private MacroTotals saveProductEntry(SaveFoodEntryReq newFoodEntry) {
        ProductInfo productInfo = this.foodApiService.getProductInfo(newFoodEntry.getId());
        double calories, protein, carbs, fats;

        calories = productInfo.getNutrition().getCalories() * newFoodEntry.getQuantity();
        protein = productInfo.getNutrition().getProtein() * newFoodEntry.getQuantity();
        carbs = productInfo.getNutrition().getCarbs() * newFoodEntry.getQuantity();
        fats = productInfo.getNutrition().getFats() * newFoodEntry.getQuantity();

        return new MacroTotals(
                this.roundUp(calories),
                this.roundUp(protein),
                this.roundUp(carbs),
                this.roundUp(fats)
        );
    }

    private MacroTotals saveIngredientEntry(SaveFoodEntryReq newFoodEntry) {
        IngredientInfo ingredientInfo = this.foodApiService.getIngredientInfo(newFoodEntry.getId(),
                newFoodEntry.getQuantity(), newFoodEntry.getUnit());

        return new MacroTotals(this.roundUp(ingredientInfo.getNutrition().getCalories()),
                this.roundUp(ingredientInfo.getNutrition().getProtein()),
                this.roundUp(ingredientInfo.getNutrition().getCarbs()),
                this.roundUp(ingredientInfo.getNutrition().getFats()));
    }

    private MacroTotals saveRecipeEntry(SaveFoodEntryReq newFoodEntry, User user) {
        SpoonacularRecipe recipeInfo = this.foodApiService.searchRecipeById(newFoodEntry.getId().toString());
        this.recommendationService.sendInteractionToGorse(Interaction.ATE, user, recipeInfo);

        double calories, protein, carbs, fats;
        switch (newFoodEntry.getUnit()) {
            case "serving":
                calories = recipeInfo.getNutrition().getCalories() * newFoodEntry.getQuantity();
                protein = recipeInfo.getNutrition().getProtein() * newFoodEntry.getQuantity();
                carbs = recipeInfo.getNutrition().getCarbs() * newFoodEntry.getQuantity();
                fats = recipeInfo.getNutrition().getFats() * newFoodEntry.getQuantity();
                break;
            case "grams":
                if (recipeInfo.getNutrition().getWeightPerServing().getAmount() == 0) {
                    throw new RuntimeException("Cannot use grams as a Unit, because API doesn't provide grams for recipe");
                } else {
                    double weightPerServing = recipeInfo.getNutrition().getWeightPerServing().getAmount();

                    calories = (recipeInfo.getNutrition().getCalories() / weightPerServing) * newFoodEntry.getQuantity();
                    protein = (recipeInfo.getNutrition().getProtein() / weightPerServing) * newFoodEntry.getQuantity();
                    carbs = (recipeInfo.getNutrition().getCarbs() / weightPerServing) * newFoodEntry.getQuantity();
                    fats = (recipeInfo.getNutrition().getFats() / weightPerServing) * newFoodEntry.getQuantity();
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
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }

}
