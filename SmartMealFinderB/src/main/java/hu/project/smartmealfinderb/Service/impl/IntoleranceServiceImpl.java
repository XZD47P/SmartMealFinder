package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.UserIntolerance;
import hu.project.smartmealfinderb.Repository.IntoleranceRepository;
import hu.project.smartmealfinderb.Repository.UserIntoleranceRepository;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IntoleranceServiceImpl implements IntoleranceService {

    private final IntoleranceRepository intoleranceRepository;
    private final UserIntoleranceRepository userIntoleranceRepository;
    private final UserService userService;

    @Override
    public Long countIntolerances() {
        return this.intoleranceRepository.count();
    }

    @Override
    public void saveIntolerance(String label, String apiValue) {
        try {
            if (label == null) {
                throw new RuntimeException("label is null");
            } else if (apiValue == null) {
                throw new RuntimeException("apiValue is null");
            }
            this.intoleranceRepository.save(new Intolerance(label, apiValue));
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while saving intolerance: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while saving intolerance: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Intolerance> findAll() {
        try {
            return this.intoleranceRepository.findAll();
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching all intolerances: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> findByUser() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("user is null");
            }
            List<UserIntolerance> userIntolerances = this.userIntoleranceRepository.findByUser(user);

            List<String> intolerances = userIntolerances.stream()
                    .map(userIntolerance -> userIntolerance.getIntolerance().getApiValue())
                    .toList();

            return intolerances;
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while fetching user intolerances: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while fetching user intolerances: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifyIntoleranceToUser(List<String> intolerances) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            if (user == null) {
                throw new RuntimeException("user is null");
            }
            //Bejövő intoleranciák intolerance-á alakítása
            List<Intolerance> requestedIntolerances = intolerances.stream()
                    .map(intolerance -> this.intoleranceRepository.findByApiValue(intolerance)
                            .orElseThrow(() -> new RuntimeException("Unknown food intolerance option: " + intolerance)))
                    .toList();

            //Jelenleg mentett intoleranciák betöltése
            List<UserIntolerance> currentUserIntolerances = this.userIntoleranceRepository.findAllByUser(user);
            List<Intolerance> currentIntolerances = currentUserIntolerances.stream()
                    .map(UserIntolerance::getIntolerance)
                    .toList();

            //Új intoleranciák meghatározása
            List<Intolerance> addedIntolerances = requestedIntolerances.stream()
                    .filter(intolerance -> !currentIntolerances.contains(intolerance))
                    .toList();

            //Törölt intoleranciák meghatározása
            List<Intolerance> removedIntolerances = currentIntolerances.stream()
                    .filter(intolerance -> !requestedIntolerances.contains(intolerance))
                    .toList();

            //Új intoleranciák mentése a felhasználóhoz
            if (!addedIntolerances.isEmpty()) {
                this.saveIntoleranceToUser(user, addedIntolerances);
            }

            //Törölt intolernaciák törlése a felhasználótól
            if (!removedIntolerances.isEmpty()) {
                this.deleteIntoleranceFromUser(user, removedIntolerances);
            }

            //Ha üres volt a bejövő kérés, akkor minden intolerancia törlése a felhasználótól
            if (requestedIntolerances.isEmpty()) {
                this.userIntoleranceRepository.deleteAll(currentUserIntolerances);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while modifying user intolerances: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while modifying user intolerances: " + e.getMessage(), e);
        }
    }

    private void deleteIntoleranceFromUser(User user, List<Intolerance> removedIntolerances) {
        for (Intolerance intolerance : removedIntolerances) {
            this.userIntoleranceRepository.deleteByUserAndIntolerance(user, intolerance);
        }
    }

    private void saveIntoleranceToUser(User user, List<Intolerance> addedIntolerances) {
        List<UserIntolerance> userIntolerances = addedIntolerances.stream()
                .map(intolerance -> new UserIntolerance(user, intolerance))
                .toList();

        this.userIntoleranceRepository.saveAll(userIntolerances);
    }
}
