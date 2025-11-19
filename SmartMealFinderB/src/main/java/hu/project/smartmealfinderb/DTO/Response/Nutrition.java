package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

import java.util.List;

@Data
public class Nutrition {

    private List<Nutrient> nutrients;

    public double getCalories() {
        if (nutrients.isEmpty()) {
            return 0.0;
        }
        return nutrients.stream()
                .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Calories"))
                .map(Nutrient::getAmount)
                .findFirst()
                .orElse(0.0);
    }

    public double getProtein() {
        if (nutrients.isEmpty()) {
            return 0.0;
        }
        return nutrients.stream()
                .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Protein"))
                .map(Nutrient::getAmount)
                .findFirst()
                .orElse(0.0);
    }

    public double getCarbs() {
        if (nutrients.isEmpty()) {
            return 0.0;
        }
        return nutrients.stream()
                .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Carbohydrates"))
                .map(Nutrient::getAmount)
                .findFirst()
                .orElse(0.0);
    }

    public double getFats() {
        if (nutrients.isEmpty()) {
            return 0.0;
        }
        return nutrients.stream()
                .filter(nutrient -> nutrient.getName().equalsIgnoreCase("Fat"))
                .map(Nutrient::getAmount)
                .findFirst()
                .orElse(0.0);
    }

    @Data
    public static class Nutrient {
        private String name;
        private double amount;
        private String unit;
    }
}
