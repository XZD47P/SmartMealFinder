package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietGoal;
import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DietPlanRepository;
import hu.project.smartmealfinderb.Service.DietGoalService;
import hu.project.smartmealfinderb.Service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DietPlanServiceImpl implements DietPlanService {

    private static final int KGTOCALORIE = 7700; //1kg zsír nagyjából 7700 kalória

    @Autowired
    private DietGoalService dietGoalService;

    @Autowired
    private DietPlanRepository dietPlanRepository;
    private double tdee;//Total Daily Energy Expenditure = Az a kalóriaszám, amire a testünknek szüksége van az aktivitási szinthez képest
    private LocalDate goalDate;

    @Override
    public void calculateDietPlan(User user, String sex, double weight, double height, int age, int activityLevel, int goalType, double weightGoal) {

        double proteinGram, fatGram, carbsGram = 0;
        goalDate = LocalDate.now();

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

        DietGoal dietGoal = this.dietGoalService.findById(goalType);

        switch (dietGoal.getId()) {
            case 1, 2: //0.5kg vesztése hetente
                loseWeight(weight, weightGoal, dietGoal.getDeltaWeight());
                break;
            case 3: //súlyfenntartás
                break;
            case 4, 5: //0.25kg tömegnövelés hetente
                gainWeight(weight, weightGoal, dietGoal.getDeltaWeight());
                //goalDate = goalDate.plusDays(daysToReachGoal);
                break;
            default:
                throw new RuntimeException("DietGoal is invalid!");
        }

        //Forrás: https://carbonperformance.com/macros-101-how-to-gain-lose-weight-or-maintain/
        proteinGram = this.calculateProteinNeeds(tdee, dietGoal.getId());
        fatGram = this.calculateFatNeeds(tdee, dietGoal.getId());
        carbsGram = this.calculateCarbsNeeds(tdee, dietGoal.getId());

        //DietPlan dietPlan = new DietPlan(sex, height, age, goalDate, weight, weightGoal, activityLevel, tdee, proteinGram, carbsGram, fatGram, user, goalType);
        DietPlan dietPlan = new DietPlan();
        dietPlan.setSex(sex);
        dietPlan.setHeight(height);
        dietPlan.setAge(age);
        dietPlan.setGoalDate(goalDate);
        dietPlan.setStartWeight(weight);
        dietPlan.setGoalWeight(roundUp(weightGoal));
        dietPlan.setActivityLevel(activityLevel);
        dietPlan.setGoalCalorie(roundUp(this.tdee));
        dietPlan.setGoalProtein(roundUp(proteinGram));
        dietPlan.setGoalCarbohydrate(roundUp(carbsGram));
        dietPlan.setGoalFat(roundUp(fatGram));
        dietPlan.setUserId(user);
        dietPlan.setDietGoalId(dietGoal);
        dietPlanRepository.save(dietPlan);
    }

    @Override
    public DietPlan getUserDietPlan(User user) {
        return this.dietPlanRepository.findByUserId(user).orElseThrow(
                () -> new RuntimeException("User has no diet plan!")
        );
    }

    @Override
    public void deleteUserDietPlan(User user) {
        this.dietPlanRepository.deleteByUserId(user);
    }

    //Kerekítés
    private double roundUp(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }

    private double calculateCarbsNeeds(double tdee, int goalType) {
        double carbsCal;

        switch (goalType) {
            case 1, 2, 4, 5 -> {
                carbsCal = tdee * 0.45;
            }
            case 3 -> {
                carbsCal = tdee * 0.5;
            }
            default -> {
                throw new RuntimeException("GoalType is invalid!");
            }
        }

        return carbsCal / 4;
    }

    private double calculateFatNeeds(double tdee, int goalType) {
        double fatCal;

        switch (goalType) {
            case 1, 2 -> {
                fatCal = tdee * 0.3;
            }
            case 3 -> {
                fatCal = tdee * 0.2;
            }
            case 4, 5 -> {
                fatCal = tdee * 0.25;
            }
            default -> {
                throw new RuntimeException("GoalType is invalid!");
            }
        }

        return fatCal / 9;
    }

    private double calculateProteinNeeds(double tdee, int goalType) {
        double proteinCal;

        switch (goalType) {
            case 1, 2 -> {
                proteinCal = tdee * 0.25;
            }
            case 3 -> {
                proteinCal = tdee * 0.3;
            }
            case 4, 5 -> {
                proteinCal = tdee * 0.3;
            }
            default -> {
                throw new RuntimeException("GoalType is invalid!");
            }
        }

        return proteinCal / 4;
    }

    private void gainWeight(double weight, double weightGoal, double deltaWeightPerWeek) {

        if (weightGoal == 0 || weightGoal < weight) {
            throw new RuntimeException("Weight goal, days to reach goal or weight is invalid!");
        }

        double deltaWeight = weightGoal - weight;
        double days = (deltaWeight / deltaWeightPerWeek) * 7;
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.goalDate = goalDate.plusDays((long) days);
        this.tdee += deltaCalorie;
    }

    private void loseWeight(double weight, double weightGoal, double deltaWeightPerWeek) {

        if (weightGoal == 0 || weight < weightGoal) {
            throw new RuntimeException("Goal weight is invalid!");
        }

        double deltaWeight = weight - weightGoal;
        double days = (deltaWeight / deltaWeightPerWeek) * 7; //7-es szorzó a napokra váltás miatt, mivel alapvetőleg ez a hetet adná vissza
        double deltaCalorie = deltaWeight * KGTOCALORIE / days;
        this.goalDate = goalDate.plusDays((long) days);
        this.tdee -= deltaCalorie;
    }


}
