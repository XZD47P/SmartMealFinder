package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietGoalRepository extends JpaRepository<DietGoal, Integer> {
    
}
