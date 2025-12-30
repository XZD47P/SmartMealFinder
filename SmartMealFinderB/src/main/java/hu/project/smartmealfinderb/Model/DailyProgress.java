package hu.project.smartmealfinderb.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_dailyprogress_user_date", columnList = "user_id,date")
        }
)
public class DailyProgress extends AuditEntity {

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
    private double weight;
    private double caloriesConsumed;
    private double proteinConsumed;
    private double carbsConsumed;
    private double fatsConsumed;

    private String comment;

    public DailyProgress(User userId, DietPlan dietPlan, LocalDate date, double caloriesConsumed, double proteinConsumed, double carbsConsumed, double fatsConsumed) {
        this.userId = userId;
        this.dietPlan = dietPlan;
        this.date = date;
        this.caloriesConsumed = caloriesConsumed;
        this.proteinConsumed = proteinConsumed;
        this.carbsConsumed = carbsConsumed;
        this.fatsConsumed = fatsConsumed;
    }
}
