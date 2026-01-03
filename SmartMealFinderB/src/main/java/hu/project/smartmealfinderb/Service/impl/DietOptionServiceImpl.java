package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserDietOption;
import hu.project.smartmealfinderb.Repository.DietOptionRepository;
import hu.project.smartmealfinderb.Repository.UserDietOptionRepository;
import hu.project.smartmealfinderb.Service.DietOptionService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DietOptionServiceImpl implements DietOptionService {

    private final DietOptionRepository dietOptionRepository;
    private final UserDietOptionRepository userDietOptionRepository;
    private final UserService userService;

    @Override
    public Long countDietOptions() {
        return this.dietOptionRepository.count();
    }

    @Override
    public void saveDietOption(String label, String apiValue) {
        try {
            this.dietOptionRepository.save(new DietOption(label, apiValue));
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving diet option: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while saving diet option: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DietOption> findAll() {
        try {
            return this.dietOptionRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching all diet options: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> findByUser() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            List<UserDietOption> userDiets = this.userDietOptionRepository.findAllByUser(user);

            List<String> dietNames = userDiets.stream()
                    .map(userDietOption -> userDietOption.getDietOption().getApiValue())
                    .toList();

            return dietNames;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching user saved diet options: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while fetching user saved diet options: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifyDietOptionToUser(List<String> diets) {
        try {

            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("User is null");
            }
            //Bejövő diéták dietOption-né alakítása
            List<DietOption> requestedDietOptions = diets.stream()
                    .map(diet -> this.dietOptionRepository.findByApiValue(diet)
                            .orElseThrow(() -> new RuntimeException("Unknown diet option: " + diet)))
                    .toList();

            //Jelenleg mentett diétát betöltése
            List<UserDietOption> currentUserDietOptions = this.userDietOptionRepository.findAllByUser(user);
            List<DietOption> currentDietOptions = currentUserDietOptions.stream()
                    .map(UserDietOption::getDietOption)
                    .toList();

            //Új diéták meghatározása
            List<DietOption> addedDietOptions = requestedDietOptions.stream()
                    .filter(dietOption -> !currentDietOptions.contains(dietOption))
                    .toList();

            //Törölt diéták meghatározása
            List<DietOption> removedDietOptions = currentDietOptions.stream()
                    .filter(dietOption -> !requestedDietOptions.contains(dietOption))
                    .toList();

            //Új diéták mentése a felhasználóhoz
            if (!addedDietOptions.isEmpty()) {
                this.saveDietOptionToUser(user, addedDietOptions);
            }

            //Törölt diéták törlése a felhasználótól
            if (!removedDietOptions.isEmpty()) {
                this.deleteDietOptionFromUser(user, removedDietOptions);
            }

            //Ha üres volt a bejövő kérés, akkor minden diéta törlése a felhasználótól
            if (requestedDietOptions.isEmpty()) {
                this.userDietOptionRepository.deleteAll(currentUserDietOptions);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving diet option to user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while trying to save the diet options for user: " + e.getMessage(), e);
        }
    }

    private void deleteDietOptionFromUser(User user, List<DietOption> deletedDietOptions) {
        for (DietOption dietOption : deletedDietOptions) {
            this.userDietOptionRepository.deleteByUserAndDietOption(user, dietOption);
        }
    }

    private void saveDietOptionToUser(User user, List<DietOption> newDietOptions) {
        List<UserDietOption> userDietOptions = newDietOptions.stream()
                .map(dietOption -> new UserDietOption(user, dietOption))
                .toList();

        this.userDietOptionRepository.saveAll(userDietOptions);
    }
}
