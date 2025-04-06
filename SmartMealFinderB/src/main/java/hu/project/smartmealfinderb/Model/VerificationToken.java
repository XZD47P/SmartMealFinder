package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String verificationToken;

    private Instant expiryDate;

    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationToken(User user, String verificationToken, Instant expiryDate) {
        this.user = user;
        this.verificationToken = verificationToken;
        this.expiryDate = expiryDate;
    }
}
