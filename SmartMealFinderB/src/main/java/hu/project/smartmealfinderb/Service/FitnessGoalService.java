package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Model.FitnessGoal;

import java.util.List;

public interface FitnessGoalService {
    FitnessGoal findById(int goalType);

    List<FitnessGoal> findAll();
}
