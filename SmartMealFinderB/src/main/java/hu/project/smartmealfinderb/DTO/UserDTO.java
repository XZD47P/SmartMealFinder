package hu.project.smartmealfinderb.DTO;

import hu.project.smartmealfinderb.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean accountNonLocked;
    private boolean accountVerified;
    private Instant verificationDeadline;
    private String signupMethod;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
