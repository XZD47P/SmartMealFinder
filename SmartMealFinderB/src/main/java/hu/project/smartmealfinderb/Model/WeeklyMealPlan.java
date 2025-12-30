package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "weekly_meal_plan", indexes = {
        @Index(name = "idx_user_year_week", columnList = "user_id, planningYear, weekNumber")
})
public class WeeklyMealPlan extends AuditEntity {

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
    private int servings;
    @Lob
    private String ingredientsJson;
}
