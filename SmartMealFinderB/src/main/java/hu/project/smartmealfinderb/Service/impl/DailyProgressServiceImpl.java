package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DailyProgressRepository;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyProgressServiceImpl implements DailyProgressService {

    private final DailyProgressRepository dailyProgressRepository;
    private final UserService userService;
    private final DietPlanService dietPlanService;
    private LocalDate date;

    @Override
    public DailyProgress findTodayProgress(User user) {
        try {
            if (user == null) {
                throw new RuntimeException("User cannot be null");
            }
            date = LocalDate.now();
            return this.dailyProgressRepository.findByUserIdAndDate(user, date).orElse(null);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching today progress: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while trying to find user today progress: " + e.getMessage(), e);
        }
    }

    @Override
    public void createTodayProgress(User user, DietPlan plan, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed) {
        try {
            if (user == null) {
                throw new RuntimeException("User cannot be null");
            }
            if (plan == null) {
                throw new RuntimeException("Diet plan cannot be null");
            }
            date = LocalDate.now();
            DailyProgress dailyProgress = new DailyProgress(user, plan, date, caloriesConsumed, proteinConsumed, carbsConsumed, fatsConsumed);

            this.dailyProgressRepository.save(dailyProgress);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while creating today progress: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while trying to create user today progress: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateTodayProgress(DailyProgress existingProgress, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed) {
        try {
            existingProgress.setCaloriesConsumed(caloriesConsumed);
            existingProgress.setProteinConsumed(proteinConsumed);
            existingProgress.setCarbsConsumed(carbsConsumed);
            existingProgress.setFatsConsumed(fatsConsumed);
            this.dailyProgressRepository.save(existingProgress);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while updating today progress: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DailyProgress> findAll() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            return this.dailyProgressRepository.findAllByUserId(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching user's all daily progress: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while trying to fetch user's all daily progress: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserProgression(User user) {
        try {
            if (user == null) {
                throw new RuntimeException("User cannot be null");
            }
            this.dailyProgressRepository.deleteByUserId(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while deleting user's all daily progress: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while deleting user progression: " + e.getMessage(), e);
        }
    }

    @Override
    public void saveWeight(double weight, String comment) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User cannot be null");
            }
            DietPlan dietPlan = this.dietPlanService.getUserDietPlan(user);
            DailyProgress dailyProgress = findTodayProgress(user);

            if (dailyProgress == null) {
                DailyProgress newDailyProgress = new DailyProgress();

                newDailyProgress.setDate(this.date);
                newDailyProgress.setDietPlan(dietPlan);
                newDailyProgress.setUserId(user);
                newDailyProgress.setWeight(weight);
                newDailyProgress.setComment(comment);

                this.dailyProgressRepository.save(newDailyProgress);
            } else {
                dailyProgress.setWeight(weight);
                dailyProgress.setComment(comment);
                this.dailyProgressRepository.save(dailyProgress);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving user's weight: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving weight: " + e.getMessage());
        }
    }

    @Override
    public DailyProgress getTodayProgress() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            return this.findTodayProgress(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching user's today progress: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching user's today progress: " + e.getMessage(), e);
        }
    }
}
