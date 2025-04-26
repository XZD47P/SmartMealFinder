package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DietPlanRepository;
import hu.project.smartmealfinderb.Service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DietPlanServiceImpl implements DietPlanService {

    private static final int KGTOCALORIE = 7700; //1kg zsír nagyjából 7700 kalória
    @Autowired
    private DietPlanRepository dietPlanRepository;
    private double tdee;//Total Daily Energy Expenditure = Az a kalóriaszám, amire a testünknek szüksége van az aktivitási szinthez képest

    @Override
    public void calculateDietPlan(User user, String sex, double weight, double height, int age, int activityLevel, int goalType, double weightGoal, int daysToReachGoal) {

        double proteinGram, fatGram, carbsGram = 0;
        LocalDate goalDate = LocalDate.now();

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
                goalDate = goalDate.plusDays(daysToReachGoal);
                break;
            case 3:
                gainWeight(weight, weightGoal, daysToReachGoal);
                goalDate = goalDate.plusDays(daysToReachGoal);
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        //Forrás: https://carbonperformance.com/macros-101-how-to-gain-lose-weight-or-maintain/
        proteinGram = this.calculateProteinNeeds(tdee, goalType);
        fatGram = this.calculateFatNeeds(tdee, goalType);
        carbsGram = this.calculateCarbsNeeds(tdee, goalType);

        DietPlan dietPlan = new DietPlan(sex, height, age, goalDate, weight, weightGoal, activityLevel, tdee, proteinGram, carbsGram, fatGram, user, goalType);
        dietPlanRepository.save(dietPlan);
    }

    private double calculateCarbsNeeds(double tdee, int goalType) {
        double carbsCal;

        switch (goalType) {
            case 1:
                carbsCal = tdee * 0.5;
                break;
            case 2:
                carbsCal = tdee * 0.45;
                break;
            case 3:
                carbsCal = tdee * 0.45;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return carbsCal / 4;
    }

    private double calculateFatNeeds(double tdee, int goalType) {
        double fatCal;

        switch (goalType) {
            case 1:
                fatCal = tdee * 0.2;
                break;
            case 2:
                fatCal = tdee * 0.3;
                break;
            case 3:
                fatCal = tdee * 0.25;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return fatCal / 9;
    }

    private double calculateProteinNeeds(double tdee, int goalType) {
        double proteinCal;

        switch (goalType) {
            case 1:
                proteinCal = tdee * 0.3;
                break;
            case 2:
                proteinCal = tdee * 0.25;
                break;
            case 3:
                proteinCal = tdee * 0.3;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return proteinCal / 4;
    }

    private void gainWeight(double weight, double weightGoal, int days) {

        if (weightGoal == 0 || days == 0 || weightGoal < weight) {
            throw new RuntimeException("Weight goal, days to reach goal or weight is invalid!");
        }

        double deltaWeight = weightGoal - weight;
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.tdee += deltaCalorie;
    }

    private void loseWeight(double weight, double weightGoal, int days) {

        if (weightGoal == 0 || days == 0 || weight < weightGoal) {
            throw new RuntimeException("Weight goal, days to reach goal or weight is invalid!");
        }

        double deltaWeight = weight - weightGoal;
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.tdee -= deltaCalorie;
    }


}
