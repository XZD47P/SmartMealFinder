package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.Intolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntoleranceRepository extends JpaRepository<Intolerance, Long> {
}
