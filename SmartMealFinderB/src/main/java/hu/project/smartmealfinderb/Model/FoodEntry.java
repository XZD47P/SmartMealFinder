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
                @Index(name = "idx_foodenty_dailyprogress", columnList = "dailyprogress_id")
        }
)
public class FoodEntry extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "dailyprogress_id")
    private DailyProgress dailyProgress;

    @Column(name = "spoonacular_id")
    private Long spoonacularId;
    private String name;
    private String category;
    private double quantity;
    private String unit;
    private double calories;
    private double protein;
    private double carbs;
    private double fats;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


}
