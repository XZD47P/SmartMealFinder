package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;

import java.util.List;

public interface DailyProgressService {

    DailyProgress findTodayProgress(User userId);

    void createTodayProgress(User user, DietPlan plan, double weight, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed, String comment);

    void updateTodayProgress(DailyProgress existingProgress, double weight, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed, String comment);

    List<DailyProgress> findAll(User user);
}
