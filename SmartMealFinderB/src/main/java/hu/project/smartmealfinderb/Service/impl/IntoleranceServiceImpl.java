package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserIntolerance;
import hu.project.smartmealfinderb.Repository.IntoleranceRepository;
import hu.project.smartmealfinderb.Repository.UserIntoleranceRepository;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntoleranceServiceImpl implements IntoleranceService {

    @Autowired
    private IntoleranceRepository intoleranceRepository;

    @Autowired
    private UserIntoleranceRepository userIntoleranceRepository;

    @Override
    public Long countIntolerances() {
        return this.intoleranceRepository.count();
    }

    @Override
    public void saveIntolerance(String label, String apiValue) {
        this.intoleranceRepository.save(new Intolerance(label, apiValue));
    }

    @Override
    public List<Intolerance> findAll() {
        return this.intoleranceRepository.findAll();
    }

    @Override
    public List<String> findByUser(User user) {
        List<UserIntolerance> userIntolerances = this.userIntoleranceRepository.findByUser(user);

        List<String> intolerances = userIntolerances.stream()
                .map(userIntolerance -> userIntolerance.getIntolerance().getApiValue())
                .toList();

        return intolerances;
    }
}
