package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.Security.Response.LoginResponse;

import java.util.Set;

public interface UserService {
    void registerUser(String email, String username, String password, Set<String> role);

    LoginResponse authenticateUser(String username, String password);
}
