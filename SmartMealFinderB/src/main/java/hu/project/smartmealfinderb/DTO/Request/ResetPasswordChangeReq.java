package hu.project.smartmealfinderb.DTO.Request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordChangeReq {

    private String token;

    @Size(min = 6)
    private String newPassword;
}
