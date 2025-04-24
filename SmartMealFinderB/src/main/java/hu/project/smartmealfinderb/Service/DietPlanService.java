package hu.project.smartmealfinderb.Service;

public interface DietPlanService {

    double calculateDietPlan(String sex, float weight, float height, int age, int activityLevel, int goalType, float weightGoal, int daysToReachGoal);
}
