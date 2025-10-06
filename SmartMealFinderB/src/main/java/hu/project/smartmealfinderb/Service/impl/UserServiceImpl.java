package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.UserDTO;
import hu.project.smartmealfinderb.DTO.UserInfoResponse;
import hu.project.smartmealfinderb.Model.*;
import hu.project.smartmealfinderb.Repository.PasswordResetTokenRepository;
import hu.project.smartmealfinderb.Repository.RoleRepository;
import hu.project.smartmealfinderb.Repository.UserRepository;
import hu.project.smartmealfinderb.Repository.VerificationTokenRepository;
import hu.project.smartmealfinderb.Security.JWT.JwtUtils;
import hu.project.smartmealfinderb.Security.Response.LoginResponse;
import hu.project.smartmealfinderb.Service.EmailService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void registerUser(String email, String username, String password, Set<String> role, String firstName, String lastName) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("There was an error while registering user: " + e.getMessage());
        }
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

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isAccountNonLocked(),
                user.isAccountVerified(),
                user.getVerificationDeadline(),
                roles
        );

        return response;

    }

    @Override
    public void generatePasswordResetToken(String email) {
        try {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = UUID.randomUUID().toString();
            Instant expiryDate = Instant.now().plus(15, ChronoUnit.MINUTES);
            PasswordResetToken passwordResetToken = new PasswordResetToken(user, token, expiryDate);
            this.passwordResetTokenRepository.save(passwordResetToken);

            //Email küldése
            String resetURL = this.frontendUrl + "/reset-password?token=" + token;
            this.emailService.sendPasswordResetEmail(user.getUserName(), email, resetURL);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating password reset token: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        try {
            PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

            if (resetToken.isUsed()) {
                throw new RuntimeException("Password reset token has already been used");
            }

            if (resetToken.getExpiryDate().isBefore(Instant.now())) {
                throw new RuntimeException("Password reset token has expired");
            }

            User user = resetToken.getUser();
            user.setPassword(this.passwordEncoder.encode(newPassword));
            this.userRepository.save(user);

            resetToken.setUsed(true);
            this.passwordResetTokenRepository.save(resetToken);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while resetting password: " + e.getMessage());
        }

    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        AppRole appRole = AppRole.valueOf(roleName);
        Role role = this.roleRepository.findByRoleName(appRole).orElseThrow(
                () -> new RuntimeException("Role not found")
        );

        user.setRole(role);
        this.userRepository.save(user);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        return this.convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountVerified(),
                user.getVerificationDeadline(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    //TODO: Role Service létrehozása
    @Override
    public List<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    @Override
    public void updateAccountLockedStatus(Long userId, boolean lock) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setAccountNonLocked(!lock);
        this.userRepository.save(user);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        try {
            User user = this.userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("User not found, while trying to update password")
            );
            user.setPassword(this.passwordEncoder.encode(password));
            this.userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Password update failed: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    //Oauth2 user regisztráció
    @Override
    public void registerUser(User newUser) {
        if (newUser.getPassword() != null) {
            newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
        }

        this.userRepository.save(newUser);
    }

    @Override
    public void changePassword(String jwtToken, String oldPassword, String newPassword) {
        try {
            String username = this.jwtUtils.getUserNameFromJwtToken(jwtToken);

            User user = this.userRepository.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

//        String encodedOldPassword = this.passwordEncoder.encode(oldPassword);

            if (!this.passwordEncoder.matches(oldPassword, user.getPassword())) {
                throw new RuntimeException("Old password does not match");
            }
            String encodedNewPassword = this.passwordEncoder.encode(newPassword);

            user.setPassword(encodedNewPassword);
            this.userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while changing password: " + e.getMessage());
        }
    }

    @Override
    public void generateVerificationToken(String email) {
        try {
            User user = this.userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = UUID.randomUUID().toString();
            Instant expiryDate = user.getVerificationDeadline();
            VerificationToken verificationToken = new VerificationToken(user, token, expiryDate);

            this.verificationTokenRepository.save(verificationToken);

            //Email küldése
            String verificationURL = this.frontendUrl + "/verification?token=" + token;
            this.emailService.sendVerificationEmail(user.getUserName(), email, verificationURL);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating verification token: " + e.getMessage());
        }
    }

    @Override
    public void verifyUser(String token) {
        try {
            VerificationToken verificationToken = this.verificationTokenRepository.findByVerificationToken(token)
                    .orElseThrow(() -> new RuntimeException("Invalid verification token"));

            if (verificationToken.isUsed()) {
                throw new RuntimeException("User already verified");
            }

            User user = verificationToken.getUser();
            user.setAccountVerified(true);
            this.userRepository.save(user);

            verificationToken.setUsed(true);
            this.verificationTokenRepository.save(verificationToken);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while verifying user: " + e.getMessage());
        }
    }

    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUserName(username).orElseThrow(
                () -> new RuntimeException("User not found")
        );
    }

    @Override
    public long count() {
        return this.userRepository.count();
    }

    @Override
    public void updateAccountVerificationStatus(Long userId, boolean verification) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setAccountVerified(verification);
        this.userRepository.save(user);
    }
}
