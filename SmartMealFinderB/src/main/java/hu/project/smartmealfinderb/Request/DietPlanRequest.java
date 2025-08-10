package hu.project.smartmealfinderb.Request;

import lombok.Data;

@Data
public class DietPlanRequest {

    private String sex;
    private double weight;
    private double height;
    private int age;
    private int activityLevel;
    private int goalType;
    private float weightGoal;

}
