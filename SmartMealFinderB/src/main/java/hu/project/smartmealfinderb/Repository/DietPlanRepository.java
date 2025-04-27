package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    Optional<DietPlan> findByUserId(User userId);
}
