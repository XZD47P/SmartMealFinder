package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietOptionRepository extends JpaRepository<DietOption, Long> {
}
