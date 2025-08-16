package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    void deleteByUser(User user);
}
