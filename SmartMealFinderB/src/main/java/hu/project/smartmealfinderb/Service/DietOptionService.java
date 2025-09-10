package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DietOption;

import java.util.List;

public interface DietOptionService {
    Long count();

    void save(String label, String apiValue);

    List<DietOption> findAll();
}
