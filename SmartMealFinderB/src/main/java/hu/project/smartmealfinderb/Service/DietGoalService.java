package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.DietGoal;

import java.util.List;

public interface DietGoalService {
    DietGoal findById(int goalType);

    List<DietGoal> findAll();
}
