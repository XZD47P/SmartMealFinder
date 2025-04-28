package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DailyProgressRepository;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DailyProgressServiceImpl implements DailyProgressService {

    private LocalDate date = LocalDate.now();

    @Autowired
    private DailyProgressRepository dailyProgressRepository;

    @Override
    public boolean existsTodayProgress(User user) {
        return this.dailyProgressRepository.existsByUserIdAndDate(user, date);
    }

    @Override
    public void createTodayProgress(User user, DietPlan plan, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment) {
        DailyProgress dailyProgress = new DailyProgress();
        dailyProgress.setUserId(user);
        dailyProgress.setDietPlan(plan);
        dailyProgress.setDate(date);
        dailyProgress.setWeight(weight);
        dailyProgress.setCaloriesConsumed(caloriesConsumed);
        dailyProgress.setProteinConsumed(proteinConsumed);
        dailyProgress.setCarbsConsumed(carbsConsumed);
        dailyProgress.setFatsConsumed(fatsConsumed);
        dailyProgress.setComment(comment);
        this.dailyProgressRepository.save(dailyProgress);

    }
}
