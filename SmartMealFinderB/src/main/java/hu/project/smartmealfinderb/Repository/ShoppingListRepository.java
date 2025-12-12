package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.ShoppingList;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    void deleteByUserAndPlanningYearAndWeekNumber(User user, int planningYear, int weekNumber);
}
