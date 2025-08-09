package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.UserDTO;
import hu.project.smartmealfinderb.DTO.UserInfoResponse;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    void registerUser(String email, String username, String password, Set<String> role, String firstName, String lastName);

    LoginResponse authenticateUser(String username, String password);

    UserInfoResponse getUserInfo(UserDetails userDetails);

    void generatePasswordResetToken(String email);

    void resetPassword(String token, String newPassword);

    List<User> getAllUsers();

    void updateUserRole(Long userId, String roleName);

    UserDTO getUserById(Long userId);

    List<Role> getAllRoles();

    void updateAccountLockedStatus(Long userId, boolean lock);

    void updatePassword(Long userId, String password);

    Optional<User> findByEmail(String email);

    void registerUser(User newUser);

    void changePassword(String jwtToken, String oldPassword, String newPassword);

    void generateVerificationToken(String email);

    void verifyUser(String token);

    User findByUsername(String username);

    long count();
}
