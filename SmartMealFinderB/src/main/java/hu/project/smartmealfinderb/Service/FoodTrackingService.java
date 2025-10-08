package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;

public interface FoodTrackingService {
    void saveFoodEntry(SaveFoodEntryReq newFoodEntry);

    void deleteFoodEntry(Long foodEntryId);
}
