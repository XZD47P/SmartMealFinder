package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserIntolerance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "intolerance_id", nullable = false)
    private Intolerance intolerance;

    public UserIntolerance(User user, Intolerance intolerance) {
        this.user = user;
        this.intolerance = intolerance;
    }
}
