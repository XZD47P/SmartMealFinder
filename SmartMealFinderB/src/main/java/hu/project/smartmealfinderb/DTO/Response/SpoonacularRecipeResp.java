package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class SpoonacularRecipeResp {

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
    private String instructions;

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

}
