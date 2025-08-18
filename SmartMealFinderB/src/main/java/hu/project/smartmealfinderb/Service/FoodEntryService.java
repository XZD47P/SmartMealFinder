package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;

import java.util.List;

public interface FoodEntryService {
    void addFoodEntry(User user, DailyProgress dailyProgress, Long spoonacularId, String name, double calories, double protein, double carbs, double fats);

    void deleteAllUserFoodEntries(User user);

    List<FoodEntry> findAllTodayEntryByUser(User user);

    FoodEntry findById(Long foodEntryId);

    void deleteById(Long foodEntryId);
}
