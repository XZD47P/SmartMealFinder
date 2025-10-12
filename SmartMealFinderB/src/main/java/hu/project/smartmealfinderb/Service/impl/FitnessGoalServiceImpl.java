package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Repository.FitnessGoalRepository;
import hu.project.smartmealfinderb.Service.FitnessGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessGoalServiceImpl implements FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;

    @Override
    public FitnessGoal findById(int goalType) {
        try {
            return this.fitnessGoalRepository.findById(goalType).orElseThrow(
                    () -> new RuntimeException("DietGoal not found with id " + goalType)
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while serching fitness goal: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for fitness goal: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FitnessGoal> findAll() {
        try {
            return this.fitnessGoalRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching fitness goals: " + e.getMessage(), e);
        }
    }
}
