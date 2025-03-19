package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Repository.UserRepository;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> registerUser(String email, String username, String password, Set<String> role) {
        if (this.userRepository.existsByUserName(username)) {
            throw new RuntimeException("Username is already in use");
        }
        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered");
        }

        User newUser = new User(email, username, passwordEncoder.encode(password));
        Set<String> strRoles = role;
        Optional<Role> userRole;

        if (strRoles == null || strRoles.isEmpty()) {
            userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER);
        } else {
            String roleName = strRoles.iterator().next();
            if (roleName.equals("admin")) {
                userRole = this.roleRepository.findByRoleName(AppRole.ROLE_ADMIN);
            } else if (roleName.equals("user")) {
                userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER);
            } else {
                throw new RuntimeException("Invalid role");
            }
        }

        //TODO: User Role-hoz rendelése
        newUser.setRole(userRole);
        newUser.setSignUpMethod("email");

        this.userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }
}
