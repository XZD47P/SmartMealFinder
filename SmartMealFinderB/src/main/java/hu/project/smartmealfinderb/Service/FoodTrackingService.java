package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Request.SaveFoodEntryReq;

public interface FoodTrackingService {
    void saveFoodEntry(String username, SaveFoodEntryReq newFoodEntry);
}
