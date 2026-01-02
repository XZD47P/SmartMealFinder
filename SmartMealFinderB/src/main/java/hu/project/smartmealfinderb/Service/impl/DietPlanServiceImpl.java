package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietPlan;
import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.DietPlanRepository;
import hu.project.smartmealfinderb.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DietPlanServiceImpl implements DietPlanService {

    private static final int KGTOCALORIE = 7700; //1kg zsír nagyjából 7700 kalória
    private final FitnessGoalService fitnessGoalService;
    private final DietPlanRepository dietPlanRepository;
    private final FoodEntryService foodEntryService;
    private final UserService userService;
    @Lazy
    @Autowired
    private DailyProgressService dailyProgressService;
    private double tdee;//Total Daily Energy Expenditure = Az a kalóriaszám, amire a testünknek szüksége van az aktivitási szinthez képest
    private LocalDate goalDate;

    @Override
    public void calculateDietPlan(String sex, double weight, double height, int age, int activityLevel, int goalType, double weightGoal) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            double proteinGram, fatGram, carbsGram = 0;
            goalDate = LocalDate.now();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
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

            FitnessGoal fitnessGoal = this.fitnessGoalService.findById(goalType);

            switch (fitnessGoal.getGoal().toLowerCase()) {
                case "lose":
                    loseWeight(weight, weightGoal, fitnessGoal.getDeltaWeight());
                    break;
                case "maintain":
                    break;
                case "gain":
                    gainWeight(weight, weightGoal, fitnessGoal.getDeltaWeight());
                    break;
                default:
                    throw new RuntimeException("DietGoal is invalid!");
            }

            this.validateHealthLimits(sex, age, height, weight, weightGoal, this.tdee, fitnessGoal.getGoal().toLowerCase());

            //Forrás: https://carbonperformance.com/macros-101-how-to-gain-lose-weight-or-maintain/
            proteinGram = this.calculateProteinNeeds(tdee, fitnessGoal.getGoal().toLowerCase());
            fatGram = this.calculateFatNeeds(tdee, fitnessGoal.getGoal().toLowerCase());
            carbsGram = this.calculateCarbsNeeds(tdee, fitnessGoal.getGoal().toLowerCase());

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
            dietPlan.setFitnessGoalId(fitnessGoal);
            dietPlanRepository.save(dietPlan);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while calculating diet plan: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while calculating the diet plan: " + e.getMessage());
        }
    }

    private void validateHealthLimits(String sex, int age, double heightCm, double weight, double weightGoal, double calculatedCalories, String goalType) {

        //Helyes adatbevitel ellenőrzése
        if (heightCm < 100 || heightCm > 250) {
            throw new RuntimeException("The provided height value is invalid!");
        }
        if (weight < 30 || weight > 300) {
            throw new RuntimeException("The provided weight value is invalid!");
        }
        
        //BMI Ellenőzés
        double heightM = heightCm / 100.0;
        double currentBMI = weight / (heightM * heightM);
        double goalBMI = weightGoal / (heightM * heightM);

        //17 az enyhe soványság, nem engedi a felhasználót tovább fogyni
        if (goalType.equals("lose") && currentBMI < 17) {
            throw new RuntimeException("You are below the healthy BMI value (17). Losing weight is not recommended!");
        }

        // Ha a kitűzött cél veszélyesen alacsony BMI-t eredményezne
        if (goalBMI < 17) {
            throw new RuntimeException("Your goal weight is below the healthy BMI value (17)!");
        }

        //Kalóriaminimum Ellenőrzése
        double minsafeCalories;
        if (sex.equalsIgnoreCase("male")) {
            minsafeCalories = 1500;
        } else {
            minsafeCalories = 1200;
        }

        if (calculatedCalories < minsafeCalories) {
            throw new RuntimeException("The calculated daily calorie intake is less then safety limit!");
        }
    }

    @Override
    public DietPlan getUserDietPlan(User user) {
        try {
            return this.dietPlanRepository.findByUserId(user).orElseThrow(
                    () -> new RuntimeException("User has no diet plan!")
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while getting diet plan: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while getting diet plan: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserDietPlan() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            this.foodEntryService.deleteAllUserFoodEntries(user);
            this.dailyProgressService.deleteUserProgression(user);
            this.dietPlanRepository.deleteByUserId(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while deleting diet plan: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while deleting user diet plan: " + e.getMessage());
        }
    }

    @Override
    public DietPlan getCurrentUserDietPlan() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            return this.dietPlanRepository.findByUserId(user).orElse(null);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while getting user diet plan: " + e.getMessage(), e);
        }
    }

    //Kerekítés
    private double roundUp(double value) {
        return new BigDecimal(value)
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }

    private double calculateCarbsNeeds(double tdee, String goalType) {
        double carbsCal;

        switch (goalType) {
            case "lose":
                carbsCal = tdee * 0.45;
                break;
            case "maintain":
                carbsCal = tdee * 0.5;
                break;
            case "gain":
                carbsCal = tdee * 0.45;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return carbsCal / 4;
    }

    private double calculateFatNeeds(double tdee, String goalType) {
        double fatCal;

        switch (goalType) {
            case "lose":
                fatCal = tdee * 0.3;
                break;
            case "maintain":
                fatCal = tdee * 0.2;
                break;
            case "gain":
                fatCal = tdee * 0.25;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
        }

        return fatCal / 9;
    }

    private double calculateProteinNeeds(double tdee, String goalType) {
        double proteinCal;

        switch (goalType) {
            case "lose":
                proteinCal = tdee * 0.25;
                break;
            case "maintain":
                proteinCal = tdee * 0.3;
                break;
            case "gain":
                proteinCal = tdee * 0.3;
                break;
            default:
                throw new RuntimeException("GoalType is invalid!");
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
