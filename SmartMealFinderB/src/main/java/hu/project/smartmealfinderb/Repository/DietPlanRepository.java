package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {

    Optional<DietPlan> findByUserId(User userId);

    void deleteByUserId(User user);
}
