package hu.project.smartmealfinderb.Request;

import lombok.Data;

@Data
public class DietPlanRequest {

    private String sex;
    private float weight;
    private float height;
    private int age;
    private int activityLevel;
    private int goalType;
    private float weightGoal;
    private int daysToReachGoal;

}
