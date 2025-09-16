package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.FitnessGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessGoalRepository extends JpaRepository<FitnessGoal, Integer> {

}
