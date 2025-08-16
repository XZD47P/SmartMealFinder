package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WeightLogReq {

    private double weight;
    private String comment;
}
