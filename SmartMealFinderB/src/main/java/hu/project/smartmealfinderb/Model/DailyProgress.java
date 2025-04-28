package hu.project.smartmealfinderb.Model;

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
    private User user;
    @ManyToOne
    private DietPlan dietPlan;

    private LocalDate date;
    private float weight;
    private int caloriesConsumed;
    private int proteinConsumed;
    private int carbsConsumed;
    private int fatsConsumed;

    private String comment;


}
