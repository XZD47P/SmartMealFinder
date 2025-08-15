package hu.project.smartmealfinderb.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveFoodEntryReq {

    private Long spoonacularId;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
}
