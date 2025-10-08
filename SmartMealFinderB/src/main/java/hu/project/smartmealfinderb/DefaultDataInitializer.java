package hu.project.smartmealfinderb;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Repository.FitnessGoalRepository;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Service.DietOptionService;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements CommandLineRunner {

    private final UserService userService;
    //TODO: Ha lesz RoleService, akkor csere kell
    private final RoleRepository roleRepository;
    //TODO: Ha lesz Service, akkor csere
    private final FitnessGoalRepository fitnessGoalRepository;
    private final DietOptionService dietOptionService;
    private final IntoleranceService intoleranceService;

    @Override
    public void run(String... args) {
        try {
            if (this.roleRepository.count() == 0) {
                Role userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

                Role adminRole = this.roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
            }

            if (this.userService.count() == 0) {
                this.userService.registerUser("test@email.com", "test", "test123", Collections.singleton("user"), "User", "Test");
                this.userService.registerUser("admin@emal.com", "admin", "admin123", Collections.singleton("admin"), "User", "Admin");
            }

            if (this.fitnessGoalRepository.count() == 0) {
                this.fitnessGoalRepository.save(new FitnessGoal("Lose", 0.5));
                this.fitnessGoalRepository.save(new FitnessGoal("Lose", 0.25));
                this.fitnessGoalRepository.save(new FitnessGoal("Maintain", 0));
                this.fitnessGoalRepository.save(new FitnessGoal("Gain", 0.25));
                this.fitnessGoalRepository.save(new FitnessGoal("Gain", 0.5));
            }

            if (this.dietOptionService.countDietOptions() == 0) {
                this.dietOptionService.saveDietOption("Gluten Free", "gluten free");
                this.dietOptionService.saveDietOption("Ketogenic", "ketogenic");
                this.dietOptionService.saveDietOption("Vegetarian", "vegetarian");
                this.dietOptionService.saveDietOption("Lacto-Vegetarian", "lacto-vegetarian");
                this.dietOptionService.saveDietOption("Ovo-Vegetarian", "ovo-vegetarian");
                this.dietOptionService.saveDietOption("Vegan", "vegan");
                this.dietOptionService.saveDietOption("Pescetarian", "pescetarian");
                this.dietOptionService.saveDietOption("Paleo", "paleo");
                this.dietOptionService.saveDietOption("Primal", "primal");
                this.dietOptionService.saveDietOption("Low FODMAP", "low FODMAP");
                this.dietOptionService.saveDietOption("Whole30", "whole30");
            }

            if (this.intoleranceService.countIntolerances() == 0) {
                this.intoleranceService.saveIntolerance("Dairy", "dairy");
                this.intoleranceService.saveIntolerance("Egg", "egg");
                this.intoleranceService.saveIntolerance("Gluten", "gluten");
                this.intoleranceService.saveIntolerance("Grain", "grain");
                this.intoleranceService.saveIntolerance("Peanut", "peanut");
                this.intoleranceService.saveIntolerance("Seafood", "seafood");
                this.intoleranceService.saveIntolerance("Sesame", "sesame");
                this.intoleranceService.saveIntolerance("Shellfish", "shellfish");
                this.intoleranceService.saveIntolerance("Soy", "soy");
                this.intoleranceService.saveIntolerance("Sulfite", "sulfite");
                this.intoleranceService.saveIntolerance("Tree Nut", "tree nut");
                this.intoleranceService.saveIntolerance("Wheat", "wheat");
            }
        } catch (Exception e) {
            System.err.println("There was an error while trying to set up starter data: " + e.getMessage());
        }

    }
}
