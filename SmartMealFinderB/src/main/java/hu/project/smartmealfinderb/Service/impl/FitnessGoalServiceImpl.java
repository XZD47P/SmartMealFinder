package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Repository.FitnessGoalRepository;
import hu.project.smartmealfinderb.Service.FitnessGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessGoalServiceImpl implements FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;

    @Override
    public FitnessGoal findById(int goalType) {
        return this.fitnessGoalRepository.findById(goalType).orElseThrow(() -> new RuntimeException("DietGoal not found!"));
    }

    @Override
    public List<FitnessGoal> findAll() {
        return this.fitnessGoalRepository.findAll();
    }
}
