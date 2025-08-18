package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Model.User;

public interface FoodTrackingService {
    void saveFoodEntry(String username, SaveFoodEntryReq newFoodEntry);

    void deleteFoodEntry(User user, Long foodIntakeId);
}
