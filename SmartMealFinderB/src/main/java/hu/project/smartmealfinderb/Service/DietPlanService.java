package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;

public interface DietPlanService {

    void calculateDietPlan(String sex, double weight, double height, int age, int activityLevel, int goalType, double weightGoal);

    DietPlan getUserDietPlan(User user);

    void deleteUserDietPlan();

    DietPlan getCurrentUserDietPlan();
}
