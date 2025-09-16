package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.FitnessGoal;
import hu.project.smartmealfinderb.Repository.FitnessGoalRepository;
import hu.project.smartmealfinderb.Service.FitnessGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessGoalServiceImpl implements FitnessGoalService {

    @Autowired
    private FitnessGoalRepository fitnessGoalRepository;

    @Override
    public FitnessGoal findById(int goalType) {
        return this.fitnessGoalRepository.findById(goalType).orElseThrow(() -> new RuntimeException("DietGoal not found!"));
    }

    @Override
    public List<FitnessGoal> findAll() {
        return this.fitnessGoalRepository.findAll();
    }
}
