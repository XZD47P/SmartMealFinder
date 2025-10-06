package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.FoodEntryRepository;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public void deleteAllUserFoodEntries(User user) {
        try {
            this.foodEntryRepository.deleteAllByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user food entries: " + e.getMessage());
        }
    }

    @Override
    public List<FoodEntry> findAllTodayEntryByUser(User user) {
        LocalDate date = LocalDate.now();
        return this.foodEntryRepository.findAllByUserAndCreatedAt(user, date);
    }

    @Override
    public FoodEntry findById(Long foodEntryId) {
        return this.foodEntryRepository.findById(foodEntryId).orElseThrow(
                () -> new RuntimeException("Food entry not found with id " + foodEntryId));
    }

    @Override
    public void deleteById(Long foodEntryId) {
        this.foodEntryRepository.deleteById(foodEntryId);
    }
}
