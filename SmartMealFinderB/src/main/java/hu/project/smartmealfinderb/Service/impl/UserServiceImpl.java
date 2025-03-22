package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.UserInfoResponse;
import hu.project.smartmealfinderb.Model.AppRole;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Repository.UserRepository;
import hu.project.smartmealfinderb.Security.JWT.JwtUtils;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void registerUser(String email, String username, String password, Set<String> role, String firstName, String lastName) {
        if (this.userRepository.existsByUserName(username)) {
            throw new RuntimeException("Username is already in use");
        }
        if (this.userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered");
        }

        Instant verificationDeadline = Instant.now().plus(15, ChronoUnit.DAYS);

        User newUser = new User(email, username, passwordEncoder.encode(password), firstName, lastName, verificationDeadline);
        Set<String> strRoles = role;
        Role userRole;

        if (strRoles == null || strRoles.isEmpty()) {
            userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
        } else {
            String roleName = strRoles.iterator().next();
            if (roleName.equals("admin")) {
                userRole = this.roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else if (roleName.equals("user")) {
                userRole = this.roleRepository.findByRoleName(AppRole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                throw new RuntimeException("Invalid role");
            }
        }

        newUser.setRole(userRole);
        newUser.setSignUpMethod("email");

        this.userRepository.save(newUser);
    }

    @Override
    public LoginResponse authenticateUser(String username, String password) {

        Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = this.jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        return response;

    }

    @Override
    public UserInfoResponse getUserInfo(UserDetails userDetails) {
        User user = this.userRepository.findByUserName(userDetails.getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );

        return response;

    }


}
