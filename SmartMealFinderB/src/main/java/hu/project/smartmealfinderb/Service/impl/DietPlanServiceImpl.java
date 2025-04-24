package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Service.DietPlanService;
import org.springframework.stereotype.Service;

@Service
public class DietPlanServiceImpl implements DietPlanService {

    private static final int KGTOCALORIE = 7700; //1kg zsír nagyjából 7700 kalória
    private double tdee;//Total Daily Energy Expenditure = Az a kalóriaszám, amire a testünknek szüksége van az aktivitási szinthez képest

    @Override
    public double calculateDietPlan(String sex, float weight, float height, int age, int activityLevel, int goalType, float weightGoal, int daysToReachGoal) {


        if (sex.isBlank() || weight == 0 || height == 0 || age == 0 || activityLevel == 0 || goalType == 0) {
            throw new RuntimeException("One or more parameters are missing!");
        }


        //Basal Metabolic Rate = Az a kalóriaszám, ami a súly fenntartásához szükséges ha nem csinálnánk semmit
        double bmr;
        if (sex.equals("male")) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else if (sex.equals("female")) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        } else {
            throw new RuntimeException("Sex is missing or invalid!");
        }

        switch (activityLevel) {
            case 1:
                this.tdee = bmr * 1.2;
                break;
            case 2:
                this.tdee = bmr * 1.375;
                break;
            case 3:
                this.tdee = bmr * 1.55;
                break;
            case 4:
                this.tdee = bmr * 1.725;
                break;
            case 5:
                this.tdee = bmr * 1.9;
                break;
            default:
                throw new RuntimeException("ActivityLevel is invalid!");
        }

        switch (goalType) {
            case 1:
                break;
            case 2:
                loseWeight(weight, weightGoal, daysToReachGoal);
                break;
            case 3:
                gainWeight(weight, weightGoal, daysToReachGoal);
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return tdee;
    }

    private void gainWeight(float weight, float weightGoal, int days) {

        if (weightGoal == 0 || days == 0 || weightGoal < weight) {
            throw new RuntimeException("Weight goal, days to reach goal or weight is invalid!");
        }

        double deltaWeight = weightGoal - weight;
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.tdee += deltaCalorie;
    }

    private void loseWeight(float weight, float weightGoal, int days) {

        if (weightGoal == 0 || days == 0 || weight < weightGoal) {
            throw new RuntimeException("Weight goal, days to reach goal or weight is invalid!");
        }

        double deltaWeight = weight - weightGoal;
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.tdee -= deltaCalorie;
    }


}
