package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class RecipeTileDTO {
    private Long id;
    private String title;
    private String image;
    private Nutrition nutrition;

    @Data
    private static class Nutrition {
        private List<Nutrient> nutrients;
    }

    @Data
    private static class Nutrient {
        private String name;
        private double amount;
        private String unit;
    }
}
