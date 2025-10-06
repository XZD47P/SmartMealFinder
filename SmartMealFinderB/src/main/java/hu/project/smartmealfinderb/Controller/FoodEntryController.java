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

        User user = userService.findByUsername(userDetails.getUsername());
        this.foodTrackingService.saveFoodEntry(user, newFoodEntry);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Saved Successfully"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getTodayEntries(@AuthenticationPrincipal UserDetails userDetails) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        List<FoodEntry> todayFoodEntryList = this.foodEntryService.findAllTodayEntryByUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(todayFoodEntryList);
    }

    @DeleteMapping("/delete/{foodEntryId}")
    public ResponseEntity<?> deleteFoodEntry(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long foodEntryId) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        this.foodTrackingService.deleteFoodEntry(user, foodEntryId);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Deleted Successfully"));
    }
}
