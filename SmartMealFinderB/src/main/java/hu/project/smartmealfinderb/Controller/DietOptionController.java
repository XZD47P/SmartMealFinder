package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Model.DietOption;
import hu.project.smartmealfinderb.Service.DietOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/diet-option")
public class DietOptionController {

    @Autowired
    private DietOptionService dietOptionService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllDietOptions() {
        try {
            List<DietOption> dietOptions = this.dietOptionService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(dietOptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
