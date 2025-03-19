package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String username);

    boolean existsByEmail(String email);
}
