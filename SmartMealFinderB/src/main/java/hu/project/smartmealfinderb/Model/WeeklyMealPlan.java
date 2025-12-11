package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class WeeklyMealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int planningYear;
    private int weekNumber;
    private String dayOfWeek;

    private Long recipeId;
    private String title;
    private String image;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
}
