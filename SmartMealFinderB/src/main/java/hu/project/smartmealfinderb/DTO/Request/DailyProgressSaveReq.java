package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;


@Data
public class DailyProgressSaveReq {

    private double weight;
    private double caloriesConsumed;
    private double proteinConsumed;
    private double carbsConsumed;
    private double fatsConsumed;

    private String comment;
}
