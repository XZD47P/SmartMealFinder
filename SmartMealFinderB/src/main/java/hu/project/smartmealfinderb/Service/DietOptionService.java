package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DietOption;

import java.util.List;

public interface DietOptionService {
    Long countDietOptions();

    void saveDietOption(String label, String apiValue);

    List<DietOption> findAll();

    List<String> findByUser();

    void modifyDietOptionToUser(List<String> diets);
}
