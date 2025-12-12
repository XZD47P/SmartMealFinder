package hu.project.smartmealfinderb.DTO;

import hu.project.smartmealfinderb.DTO.Response.ShoppingItemDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WeeklyMealPlanDTO {

    private int year;
    private int weekNumber;
    private Map<String, List<RecipeTileDTO>> plan;
    private List<ShoppingItemDTO> shoppingList;
}
