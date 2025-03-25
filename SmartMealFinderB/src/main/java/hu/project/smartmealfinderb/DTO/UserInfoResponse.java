package hu.project.smartmealfinderb.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean accountNonLocked;
    private boolean accountVerified;
    private Instant verificationDeadline;
    private List<String> roles;


    public UserInfoResponse(Long id, String username, String email, String firstName, String lastName,
                            boolean accountNonLocked, boolean accountVerified, Instant verificationDeadline, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNonLocked = accountNonLocked;
        this.accountVerified = accountVerified;
        this.verificationDeadline = verificationDeadline;
        this.roles = roles;
    }
}
