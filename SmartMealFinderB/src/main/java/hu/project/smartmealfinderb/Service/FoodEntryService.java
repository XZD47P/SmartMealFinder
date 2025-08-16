package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.User;

public interface FoodEntryService {
    void addFoodEntry(User user, DailyProgress dailyProgress, Long spoonacularId, String name, double calories, double protein, double carbs, double fats);

    void deleteUserFoodEntries(User user);
}
