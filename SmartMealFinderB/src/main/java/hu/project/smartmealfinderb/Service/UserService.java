package hu.project.smartmealfinderb.Service;

import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface UserService {
    ResponseEntity<?> registerUser(String email, String username, String password, Set<String> role);
}
