package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.ShoppingItemDTO;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;

import java.util.List;

public interface ShoppingListService {
    List<ShoppingItemDTO> generateShoppingList(User user, int year, int week, List<WeeklyMealPlan> savedPlan);

    List<ShoppingItemDTO> getShoppingList(User user, int year, int week);
}
