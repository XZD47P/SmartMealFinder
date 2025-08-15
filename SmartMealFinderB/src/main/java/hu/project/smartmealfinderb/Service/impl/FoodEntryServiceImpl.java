package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.FoodEntryRepository;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodEntryServiceImpl implements FoodEntryService {

    @Autowired
    private FoodEntryRepository foodEntryRepository;

    @Override
    public void addFoodEntry(User user, DailyProgress dailyProgress, Long spoonacularId, String name, double calories, double protein, double carbs, double fats) {
        FoodEntry foodEntry = new FoodEntry();
        foodEntry.setDailyProgress(dailyProgress);
        foodEntry.setSpoonacularId(spoonacularId);
        foodEntry.setName(name);
        foodEntry.setCalories(calories);
        foodEntry.setProtein(protein);
        foodEntry.setCarbs(carbs);
        foodEntry.setFats(fats);
        foodEntry.setUser(user);
        this.foodEntryRepository.save(foodEntry);
    }
}
