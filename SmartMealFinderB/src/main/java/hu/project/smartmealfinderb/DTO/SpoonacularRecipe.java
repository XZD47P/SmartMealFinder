package hu.project.smartmealfinderb.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class SpoonacularRecipe {

    private Long id;
    private String title;
    private String image;
    private int readyInMinutes;
    private int servings;
    private double healthScore;
    private boolean vegeterian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private boolean veryHealthy;
    private boolean cheap;
    private boolean veryPopular;
    private boolean sustainable;
    private boolean lowFodmap;
    private List<String> dishTypes;
    private List<String> diets;
    private List<String> cuisines;
    private List<String> occasions;
    private List<Ingredient> extendedIngredients;
    private RecipeNutrition nutrition;
    private String instructions;

    @JsonIgnore
    public List<String> getIngredientNames() {
        return this.extendedIngredients.stream()
                .map(Ingredient::getName)
                .toList();
    }

    @Data
    private static class Ingredient {
        private Long id;
        private String original;
        private String name;
    }

    @Data
    public static class RecipeNutrition {
        private List<RecipeNutrient> nutrients;
        private WeightPerServing weightPerServing;

        public double getCalories() {
            return this.getByName("Calories");
        }

        public double getProtein() {
            return this.getByName("Protein");
        }

        public double getCarbs() {
            return this.getByName("Carbohydrates");
        }

        public double getFats() {
            return this.getByName("Fat");
        }

        private double getByName(String name) {
            if (nutrients == null) return 0.0;
            return nutrients.stream()
                    .filter(n -> n.getName().equalsIgnoreCase(name))
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
