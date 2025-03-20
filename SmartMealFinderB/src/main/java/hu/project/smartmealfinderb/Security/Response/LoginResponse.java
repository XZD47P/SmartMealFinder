package hu.project.smartmealfinderb.Security.Response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private String username;
    private List<String> roles;

    public LoginResponse(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
