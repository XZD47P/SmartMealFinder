package hu.project.smartmealfinderb.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class DietPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    private String sex;
    private double height;
    private int age;

    @Nullable
    private LocalDate goalDate;

    private double startWeight;
    private double goalWeight;
    private int activityLevel;
    private double goalCalorie;
    private double goalProtein;
    private double goalCarbohydrate;
    private double goalFat;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User userId;
    private int fitnessId;

    public DietPlan(String sex, double height, int age, LocalDate goalDate, double startWeight, double goalWeight, int activityLevel, double goalCalorie, double goalProtein, double goalCarbohydrate, double goalFat, User userId, int fitnessId) {
        this.sex = sex;
        this.height = height;
        this.age = age;
        this.goalDate = goalDate;
        this.startWeight = startWeight;
        this.goalWeight = goalWeight;
        this.activityLevel = activityLevel;
        this.goalCalorie = goalCalorie;
        this.goalProtein = goalProtein;
        this.goalCarbohydrate = goalCarbohydrate;
        this.goalFat = goalFat;
        this.userId = userId;
        this.fitnessId = fitnessId;
    }
}
