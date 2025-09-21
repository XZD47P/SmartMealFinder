package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.Intolerance;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.IntoleranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/intolerance")
public class IntoleranceController {

    @Autowired
    private IntoleranceService intoleranceService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllIntolerances() {
        try {
            List<Intolerance> intolerances = this.intoleranceService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(intolerances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error while listing the intolerances: " + e.getMessage()));
        }
    }
}
