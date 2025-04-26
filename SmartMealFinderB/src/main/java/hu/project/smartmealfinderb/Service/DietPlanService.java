package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.User;

public interface DietPlanService {

    void calculateDietPlan(User user, String sex, double weight, double height, int age, int activityLevel, int goalType, double weightGoal, int daysToReachGoal);
}
