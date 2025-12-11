package hu.project.smartmealfinderb.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RecipeTileDTO {
    private Long id;
    private String title;
    private String image;
    private Nutrition nutrition;

    @Data
    public static class Nutrition {
        private List<Nutrient> nutrients;
    }

    @Data
    public static class Nutrient {
        private String name;
        private double amount;
        private String unit;
    }
}
