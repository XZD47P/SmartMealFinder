package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;

@Data
public class AdminCheckboxReq {

    private Long userId;
    private boolean checked;
}
