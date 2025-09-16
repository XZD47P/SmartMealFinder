package hu.project.smartmealfinderb;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Repository.FitnessGoalRepository;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Service.DietOptionService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DefaultDataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired //TODO: Ha lesz RoleService, akkor csere kell
    private RoleRepository roleRepository;

    @Autowired //TODO: Ha lesz Service, akkor csere
    private FitnessGoalRepository fitnessGoalRepository;

    @Autowired
    private DietOptionService dietOptionService;

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
        } catch (Exception e) {
            System.err.println("There was an error while trying to set up starter data: " + e.getMessage());
        }

    }
}
