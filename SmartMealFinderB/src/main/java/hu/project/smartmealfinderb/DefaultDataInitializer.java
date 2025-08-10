package hu.project.smartmealfinderb;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.DietGoal;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Repository.DietGoalRepository;
import hu.project.smartmealfinderb.Repository.RoleRepository;
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
    private DietGoalRepository dietGoalRepository;

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

            if (this.dietGoalRepository.count() == 0) {
                this.dietGoalRepository.save(new DietGoal("Lose", 0.5));
                this.dietGoalRepository.save(new DietGoal("Lose", 0.25));
                this.dietGoalRepository.save(new DietGoal("Maintain", 0));
                this.dietGoalRepository.save(new DietGoal("Gain", 0.25));
                this.dietGoalRepository.save(new DietGoal("Gain", 0.5));
            }
        } catch (Exception e) {
            System.err.println("There was an error while trying to set up starter data: " + e.getMessage());
        }

    }
}
