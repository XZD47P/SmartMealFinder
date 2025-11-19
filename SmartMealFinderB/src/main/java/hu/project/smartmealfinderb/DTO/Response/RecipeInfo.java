package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class RecipeInfo {

    private Long id;
    private String title;
    private RecipeNutrition nutrition;
    private int servings;

    @Data
    public static class RecipeNutrition {
        private List<RecipeNutrient> nutrients;
        private WeightPerServing weightPerServing;

        public double getCalories() {
            if (nutrients.isEmpty()) {
                return 0.0;
            }
            return nutrients.stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Calories"))
                    .map(RecipeNutrient::getAmount)
                    .findFirst()
                    .orElse(0.0);
        }

        public double getProtein() {
            if (nutrients.isEmpty()) {
                return 0.0;
            }
            return nutrients.stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Protein"))
                    .map(RecipeNutrient::getAmount)
                    .findFirst()
                    .orElse(0.0);
        }

        public double getCarbs() {
            if (nutrients.isEmpty()) {
                return 0.0;
            }
            return nutrients.stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Carbohydrates"))
                    .map(RecipeNutrient::getAmount)
                    .findFirst()
                    .orElse(0.0);
        }

        public double getFats() {
            if (nutrients.isEmpty()) {
                return 0.0;
            }
            return nutrients.stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Fat"))
                    .map(RecipeNutrient::getAmount)
                    .findFirst()
                    .orElse(0.0);
        }
    }

    @Data
    public static class RecipeNutrient {
        private String name;
        private double amount;
        private String unit;
    }

    @Data
    public static class WeightPerServing {
        private double amount;
    }
}
