package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;

@Data
public class AdminPwChangeReq {

    private Long userId;
    private String password;
}
