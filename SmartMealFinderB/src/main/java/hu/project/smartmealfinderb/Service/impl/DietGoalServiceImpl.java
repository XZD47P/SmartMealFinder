package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietGoal;
import hu.project.smartmealfinderb.Repository.DietGoalRepository;
import hu.project.smartmealfinderb.Service.DietGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietGoalServiceImpl implements DietGoalService {

    @Autowired
    private DietGoalRepository dietGoalRepository;

    @Override
    public DietGoal findById(int goalType) {
        return this.dietGoalRepository.findById(goalType).orElseThrow(() -> new RuntimeException("DietGoal not found!"));
    }

    @Override
    public List<DietGoal> findAll() {
        return this.dietGoalRepository.findAll();
    }
}
