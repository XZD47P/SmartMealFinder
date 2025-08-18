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
    public void createTodayProgress(User user, DietPlan plan, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed) {
        date = LocalDate.now();
        DailyProgress dailyProgress = new DailyProgress(user, plan, date, caloriesConsumed, proteinConsumed, carbsConsumed, fatsConsumed);

        this.dailyProgressRepository.save(dailyProgress);

    }

    @Override
    public void updateTodayProgress(DailyProgress existingProgress, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed) {

        existingProgress.setCaloriesConsumed(caloriesConsumed);
        existingProgress.setProteinConsumed(proteinConsumed);
        existingProgress.setCarbsConsumed(carbsConsumed);
        existingProgress.setFatsConsumed(fatsConsumed);
        this.dailyProgressRepository.save(existingProgress);
    }

    @Override
    public List<DailyProgress> findAll(User user) {
        return this.dailyProgressRepository.findAllByUserId(user);
    }

    @Override
    public void deleteUserProgression(User user) {
        this.dailyProgressRepository.deleteByUserId(user);
    }

    @Override
    public void saveWeight(User user, DietPlan dietPlan, double weight, String comment) {
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
    }
}
