package hu.project.smartmealfinderb.Request;

import lombok.Data;


@Data
public class DailyProgressPostReq {

    private float weight;
    private int caloriesConsumed;
    private int proteinConsumed;
    private int carbsConsumed;
    private int fatsConsumed;

    private String comment;
}
