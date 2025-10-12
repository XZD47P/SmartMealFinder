package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.FoodEntryRepository;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodEntryServiceImpl implements FoodEntryService {

    private final FoodEntryRepository foodEntryRepository;
    private final UserService userService;

    @Override
    public void addFoodEntry(User user, DailyProgress dailyProgress, Long spoonacularId, String name, String category, double calories, double protein, double carbs, double fats) {
        try {
            FoodEntry foodEntry = new FoodEntry();
            foodEntry.setDailyProgress(dailyProgress);
            foodEntry.setSpoonacularId(spoonacularId);
            foodEntry.setName(name);
            foodEntry.setCategory(category);
            foodEntry.setCalories(calories);
            foodEntry.setProtein(protein);
            foodEntry.setCarbs(carbs);
            foodEntry.setFats(fats);
            foodEntry.setUser(user);
            this.foodEntryRepository.save(foodEntry);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving food entry: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllUserFoodEntries(User user) {
        try {
            this.foodEntryRepository.deleteAllByUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while deleting food entry: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting user food entries: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FoodEntry> findAllTodayEntryByUser() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            LocalDate date = LocalDate.now();
            return this.foodEntryRepository.findAllByUserAndCreatedAt(user, date);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching all todays food entry by user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all todays food entry by user: " + e.getMessage(), e);
        }
    }

    @Override
    public FoodEntry findById(Long foodEntryId) {
        try {
            return this.foodEntryRepository.findById(foodEntryId).orElseThrow(
                    () -> new RuntimeException("Food entry not found with id " + foodEntryId));
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching food entry by id: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long foodEntryId) {
        try {
            this.foodEntryRepository.deleteById(foodEntryId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while deleting food entry by id: " + e.getMessage(), e);
        }
    }
}
