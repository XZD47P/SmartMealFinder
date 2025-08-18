package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import hu.project.smartmealfinderb.Service.FoodTrackingService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/food-entry")
public class FoodEntryController {

    @Autowired
    private FoodTrackingService foodTrackingService;

    @Autowired
    private FoodEntryService foodEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFoodEntry(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody SaveFoodEntryReq newFoodEntry) {
        try {

            this.foodTrackingService.saveFoodEntry(userDetails.getUsername(), newFoodEntry);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Saved Successfully"));
        } catch (Exception e) {
            System.err.println("There was an error saving your food entry: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error saving food intake"));
        }

    }

    @GetMapping("/get")
    public ResponseEntity<?> getTodayEntries(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            List<FoodEntry> todayFoodEntryList = this.foodEntryService.findAllTodayEntryByUser(user);

            return ResponseEntity.status(HttpStatus.OK).body(todayFoodEntryList);
        } catch (Exception e) {
            System.err.println("There was an error getting today food entries: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error getting today food entries"));
        }
    }

    @DeleteMapping("/delete/{foodIntakeId}")
    public ResponseEntity<?> deleteFoodEntry(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long foodIntakeId) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            this.foodTrackingService.deleteFoodEntry(user, foodIntakeId);

            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Deleted Successfully"));
        } catch (Exception e) {
            System.err.println("There was an error deleting your food entry: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("There was an error deleting food entry"));
        }
    }
}
