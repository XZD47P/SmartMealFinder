package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
    
}
