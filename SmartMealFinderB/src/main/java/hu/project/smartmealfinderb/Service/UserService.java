package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.UserInfoResponse;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface UserService {
    void registerUser(String email, String username, String password, Set<String> role, String firstName, String lastName);

    LoginResponse authenticateUser(String username, String password);

    UserInfoResponse getUserInfo(UserDetails userDetails);
}
