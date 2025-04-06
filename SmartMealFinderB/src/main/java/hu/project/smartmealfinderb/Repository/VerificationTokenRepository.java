package hu.project.smartmealfinderb.Repository;

import hu.project.smartmealfinderb.Model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByVerificationToken(String token);
}
