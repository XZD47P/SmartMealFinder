package hu.project.smartmealfinderb.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RemainingDailyMacrosResp {

    private double remainingCalories;
    private double remainingProteins;
    private double remainingCarbs;
    private double remainingFats;
    private List<String> diets;
    private List<String> intolerances;
}
