package hu.project.smartmealfinderb.DTO.Response;

import lombok.Data;

@Data
public class IngredientInfo {

    private Long id;
    private String name;
    private Nutrition nutrition;
}
