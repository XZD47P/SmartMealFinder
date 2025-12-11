package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;

public interface WeeklyPlannerService {

    void saveWeeklyMealPlan(WeeklyMealPlanDTO weeklyMealPlanDTO);

    WeeklyMealPlanDTO getWeeklyMealPlan(int year, int week);
}
