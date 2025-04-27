package hu.project.smartmealfinderb.Request;

import lombok.Data;

@Data
public class AdminPwChangeReq {

    private Long userId;
    private String password;
}
