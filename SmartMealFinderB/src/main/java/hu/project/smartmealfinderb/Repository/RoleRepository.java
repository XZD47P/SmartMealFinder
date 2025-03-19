package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
