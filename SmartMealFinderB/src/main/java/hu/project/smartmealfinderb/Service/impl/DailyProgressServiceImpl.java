package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DailyProgressRepository;
import hu.project.smartmealfinderb.Service.DailyProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public void createTodayProgress(User user, DietPlan plan, double weight, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed, String comment) {
        date = LocalDate.now();
        DailyProgress dailyProgress = new DailyProgress(user, plan, date, weight, caloriesConsumed, proteinConsumed, carbsConsumed, fatsConsumed, comment);

        this.dailyProgressRepository.save(dailyProgress);

    }

    @Override
    public void updateTodayProgress(DailyProgress existingProgress, double weight, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed, String comment) {

        existingProgress.setWeight(weight);
        existingProgress.setCaloriesConsumed(caloriesConsumed);
        existingProgress.setProteinConsumed(proteinConsumed);
        existingProgress.setCarbsConsumed(carbsConsumed);
        existingProgress.setFatsConsumed(fatsConsumed);
        existingProgress.setComment(comment);
        this.dailyProgressRepository.save(existingProgress);
    }

    @Override
    public List<DailyProgress> findAll(User user) {
        return this.dailyProgressRepository.findAllByUserId(user);
    }
}
