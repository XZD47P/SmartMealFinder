package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.AddUserIntoleranceReq;
import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/intolerance")
@RequiredArgsConstructor
public class IntoleranceController {

    private final IntoleranceService intoleranceService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllIntolerances() {

        List<Intolerance> intolerances = this.intoleranceService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(intolerances);
    }

    @GetMapping("/load-by-user")
    public ResponseEntity<?> getUserIntolerances() {

        List<String> userIntolerances = this.intoleranceService.findByUser();
        return ResponseEntity.status(HttpStatus.OK).body(userIntolerances);
    }

    @PostMapping("/save-to-user")
    public ResponseEntity<?> saveUserDietOption(@RequestBody AddUserIntoleranceReq addUserIntoleranceReq) {

        this.intoleranceService.modifyIntoleranceToUser(addUserIntoleranceReq.getIntolerances());
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Diet option added successfully"));
    }
}
