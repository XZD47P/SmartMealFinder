package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Model.User;

import java.util.List;

public interface IntoleranceService {
    Long countIntolerances();

    void saveIntolerance(String label, String apiValue);

    List<Intolerance> findAll();

    List<String> findByUser(User user);
}
