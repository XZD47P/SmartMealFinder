package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserIntolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserIntoleranceRepository extends JpaRepository<UserIntolerance, Long> {
    List<UserIntolerance> findByUser(User user);

    List<UserIntolerance> findAllByUser(User user);

    void deleteByUserAndIntolerance(User user, Intolerance intolerance);
}
