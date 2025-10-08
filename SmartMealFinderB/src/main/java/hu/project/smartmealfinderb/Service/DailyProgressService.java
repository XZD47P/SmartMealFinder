package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;

import java.util.List;

public interface DailyProgressService {

    DailyProgress findTodayProgress(User user);

    void createTodayProgress(User user, DietPlan plan, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed);

    void updateTodayProgress(DailyProgress existingProgress, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed);

    List<DailyProgress> findAll();

    void deleteUserProgression(User user);

    void saveWeight(double weight, String comment);

    DailyProgress getTodayProgress();
}
