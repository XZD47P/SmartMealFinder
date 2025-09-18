package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserDietOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDietOptionRepository extends JpaRepository<UserDietOption, Long> {
    List<UserDietOption> findAllByUser(User user);

    void deleteByUserAndDietOption(User user, DietOption dietOption);
}
