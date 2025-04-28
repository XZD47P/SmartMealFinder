package hu.project.smartmealfinderb.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class DailyProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userId;
    
    @ManyToOne
    @JoinColumn(name = "plan_id")
    @JsonIgnore
    private DietPlan dietPlan;

    private LocalDate date;
    private float weight;
    private int caloriesConsumed;
    private int proteinConsumed;
    private int carbsConsumed;
    private int fatsConsumed;

    private String comment;

    public DailyProgress(User userId, DietPlan dietPlan, LocalDate date, float weight, int caloriesConsumed, int proteinConsumed, int carbsConsumed, int fatsConsumed, String comment) {
        this.userId = userId;
        this.dietPlan = dietPlan;
        this.date = date;
        this.weight = weight;
        this.caloriesConsumed = caloriesConsumed;
        this.proteinConsumed = proteinConsumed;
        this.carbsConsumed = carbsConsumed;
        this.fatsConsumed = fatsConsumed;
        this.comment = comment;
    }
}
