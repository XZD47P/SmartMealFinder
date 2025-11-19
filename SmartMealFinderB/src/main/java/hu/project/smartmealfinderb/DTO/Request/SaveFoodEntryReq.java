package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;

@Data
public class SaveFoodEntryReq {

    private Long id;
    private String name;
    private String type;
    private double quantity;
    private String unit;
}
