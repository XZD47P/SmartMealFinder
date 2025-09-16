package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.AdminCheckboxReq;
import hu.project.smartmealfinderb.DTO.Request.AdminPwChangeReq;
import hu.project.smartmealfinderb.DTO.Request.RoleUpdateReq;
import hu.project.smartmealfinderb.DTO.UserDTO;
import hu.project.smartmealfinderb.Model.Role;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/getusers")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers());
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody RoleUpdateReq roleUpdateReq) {
        this.userService.updateUserRole(roleUpdateReq.getUserId(), roleUpdateReq.getRole());
        return ResponseEntity.ok(new MessageResponse("User role updated successfully"));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(this.userService.getAllRoles(), HttpStatus.OK);
    }

    @PutMapping("/update-lock-status")
    public ResponseEntity<?> updateAccountLockStatus(@RequestBody AdminCheckboxReq adminCheckboxReq) {

        this.userService.updateAccountLockedStatus(adminCheckboxReq.getUserId(), adminCheckboxReq.isChecked());
        return ResponseEntity.ok(new MessageResponse("User lock status updated successfully"));
    }

    @PutMapping("/update-verification-status")
    public ResponseEntity<?> updateAccountVerificationStatus(@RequestBody AdminCheckboxReq adminCheckboxReq) {

        this.userService.updateAccountVerificationStatus(adminCheckboxReq.getUserId(), adminCheckboxReq.isChecked());
        return ResponseEntity.ok(new MessageResponse("User lock status updated successfully"));
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody AdminPwChangeReq adminPwChangeReq) {
        try {
            this.userService.updatePassword(adminPwChangeReq.getUserId(), adminPwChangeReq.getPassword());
            return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
