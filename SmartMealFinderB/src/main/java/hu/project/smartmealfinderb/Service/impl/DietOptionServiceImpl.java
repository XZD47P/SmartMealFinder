package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Repository.DietOptionRepository;
import hu.project.smartmealfinderb.Service.DietOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietOptionServiceImpl implements DietOptionService {

    @Autowired
    private DietOptionRepository dietOptionRepository;

    @Override
    public Long count() {
        return this.dietOptionRepository.count();
    }

    @Override
    public void save(String label, String apiValue) {
        this.dietOptionRepository.save(new DietOption(label, apiValue));
    }

    @Override
    public List<DietOption> findAll() {
        return this.dietOptionRepository.findAll();
    }
}
