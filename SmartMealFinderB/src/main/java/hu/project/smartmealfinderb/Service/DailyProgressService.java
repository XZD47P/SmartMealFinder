package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;

public interface DailyProgressService {

    DailyProgress findTodayProgress(User userId);

    void createTodayProgress(User user, DietPlan plan, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment);

    void updateTodayProgress(DailyProgress existingProgress, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment);
}
