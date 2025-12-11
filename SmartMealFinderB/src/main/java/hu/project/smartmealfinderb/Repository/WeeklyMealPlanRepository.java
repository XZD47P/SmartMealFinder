package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyMealPlanRepository extends JpaRepository<WeeklyMealPlan, Integer> {

    List<WeeklyMealPlan> findByUserAndPlanningYearAndWeekNumber(User user, int planningYear, int weekNumber);

    void deleteByUserAndPlanningYearAndWeekNumber(User user, int year, int weekNumber);
}
