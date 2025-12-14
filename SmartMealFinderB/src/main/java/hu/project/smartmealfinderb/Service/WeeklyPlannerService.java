package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.ShoppingItemDTO;
import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;

import java.util.List;

public interface WeeklyPlannerService {

    WeeklyMealPlanDTO saveWeeklyMealPlan(WeeklyMealPlanDTO weeklyMealPlanDTO);

    WeeklyMealPlanDTO getWeeklyMealPlan(int year, int week);

    List<ShoppingItemDTO> getWeeklyShoppingList(int year, int week);

    void toggleItemBoughtStatus(Long itemId, boolean checked);
}
