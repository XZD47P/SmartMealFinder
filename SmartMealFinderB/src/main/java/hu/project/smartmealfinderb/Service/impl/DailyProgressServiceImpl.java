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

    private LocalDate date;

    @Autowired
    private DailyProgressRepository dailyProgressRepository;

    @Override
    public DailyProgress findTodayProgress(User user) {
        date = LocalDate.now();
        return this.dailyProgressRepository.findByUserIdAndDate(user, date).orElse(null);
    }

    @Override
    public void createTodayProgress(User user, DietPlan plan, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment) {
        date = LocalDate.now();
        DailyProgress dailyProgress = new DailyProgress(user, plan, date, weight, caloriesConsumed, proteinConsumed, carbsConsumed, fatsConsumed, comment);

        this.dailyProgressRepository.save(dailyProgress);

    }

    @Override
    public void updateTodayProgress(DailyProgress existingProgress, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment) {

        existingProgress.setWeight(weight);
        existingProgress.setCaloriesConsumed(caloriesConsumed);
        existingProgress.setProteinConsumed(proteinConsumed);
        existingProgress.setCarbsConsumed(carbsConsumed);
        existingProgress.setFatsConsumed(fatsConsumed);
        existingProgress.setComment(comment);
        this.dailyProgressRepository.save(existingProgress);
    }
}
