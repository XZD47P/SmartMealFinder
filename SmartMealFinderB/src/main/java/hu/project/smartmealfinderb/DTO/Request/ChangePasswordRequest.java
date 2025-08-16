package hu.project.smartmealfinderb.DTO.Request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    private String token;
    private String oldPassword;
    private String newPassword;
}
