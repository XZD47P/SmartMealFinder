package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyMealPlanRepository extends JpaRepository<WeeklyMealPlan, Integer> {
    void deleteByUserAndPlanningYearAfterAndWeekNumber(User user, int year, int weekNumber);
}
