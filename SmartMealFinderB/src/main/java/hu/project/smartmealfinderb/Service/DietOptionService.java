package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;

import java.util.List;

public interface DietOptionService {
    Long countDietOptions();

    void saveDietOption(String label, String apiValue);

    List<DietOption> findAll();

    List<String> findByUser(User user);

    void modifyDietOptionToUser(User user, List<String> diets);
}
