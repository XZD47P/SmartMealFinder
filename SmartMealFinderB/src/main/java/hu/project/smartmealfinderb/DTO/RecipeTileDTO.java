package hu.project.smartmealfinderb.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RecipeTileDTO {
    private Long id;
    private String title;
    private String image;
    private int servings;
    private Nutrition nutrition;

    @Data
    public static class Nutrition {
        private List<Nutrient> nutrients;
        private List<Ingredient> ingredients;
    }

    @Data
    public static class Nutrient {
        private String name;
        private double amount;
        private String unit;
    }

    @Data
    public static class Ingredient {
        private Long id;
        private String name;
        private double amount;
        private String unit;
    }
}
