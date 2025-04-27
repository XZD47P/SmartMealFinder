package hu.project.smartmealfinderb.Request;

import lombok.Data;

@Data
public class RoleUpdateReq {

    private Long userId;
    private String role;
}
