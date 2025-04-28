package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DailyProgress;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyProgressRepository extends JpaRepository<DailyProgress, Long> {
    boolean existsByUserIdAndDate(User userId, LocalDate date);
}
