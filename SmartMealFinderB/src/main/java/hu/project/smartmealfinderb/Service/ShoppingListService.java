package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;

import java.util.List;

public interface ShoppingListService {
    void generateShoppingList(User user, int year, int week, List<WeeklyMealPlan> savedPlan);
}
