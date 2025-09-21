package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.UserIntolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserIntoleranceRepository extends JpaRepository<UserIntolerance, Long> {
}
