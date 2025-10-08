package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.Intolerance;

import java.util.List;

public interface IntoleranceService {
    Long countIntolerances();

    void saveIntolerance(String label, String apiValue);

    List<Intolerance> findAll();

    List<String> findByUser();

    void modifyIntoleranceToUser(List<String> intolerances);
}
