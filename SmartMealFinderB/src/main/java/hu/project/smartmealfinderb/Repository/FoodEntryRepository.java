package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    void deleteAllByUser(User user);

    List<FoodEntry> findAllByUserAndDate(User user, LocalDate date);
}
