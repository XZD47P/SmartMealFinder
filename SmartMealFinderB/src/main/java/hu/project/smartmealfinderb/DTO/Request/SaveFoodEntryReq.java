package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;

@Data
public class SaveFoodEntryReq {

    private Long spoonacularId;
    private String name;
    private String category;
    private double quantity;
    private String unit;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    private double weightPerServing;
}
