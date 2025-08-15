package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_foodenty_dailyprogress", columnList = "dailyprogress_id")
        }
)
public class FoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dailyprogress_id")
    private DailyProgress dailyProgress;

    @Column(name = "spoonacular_id")
    private Long spoonacularId;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
