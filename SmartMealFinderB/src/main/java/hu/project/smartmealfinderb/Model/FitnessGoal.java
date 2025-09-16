package hu.project.smartmealfinderb.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String goal;

    @Column(name = "delta_weight(kg)")
    private double deltaWeight;

    public FitnessGoal(String goal, double deltaWeight) {
        this.goal = goal;
        this.deltaWeight = deltaWeight;
    }
}
