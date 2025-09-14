package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserDietOption;
import hu.project.smartmealfinderb.Repository.DietOptionRepository;
import hu.project.smartmealfinderb.Repository.UserDietOptionRepository;
import hu.project.smartmealfinderb.Service.DietOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietOptionServiceImpl implements DietOptionService {

    @Autowired
    private DietOptionRepository dietOptionRepository;

    @Autowired
    private UserDietOptionRepository userDietOptionRepository;

    @Override
    public Long countDietOptions() {
        return this.dietOptionRepository.count();
    }

    @Override
    public void saveDietOption(String label, String apiValue) {
        this.dietOptionRepository.save(new DietOption(label, apiValue));
    }

    @Override
    public List<DietOption> findAll() {
        return this.dietOptionRepository.findAll();
    }

    @Override
    public List<String> findByUser(User user) {
        List<UserDietOption> userDiets = this.userDietOptionRepository.findByUser(user);

        List<String> dietNames = userDiets.stream()
                .map(userDietOption -> userDietOption.getDietOption().getApiValue())
                .toList();

        return dietNames;
    }
}
