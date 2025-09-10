package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.UserDietOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDietOptionRepository extends JpaRepository<UserDietOption, Long> {
}
