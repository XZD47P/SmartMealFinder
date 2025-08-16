package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.FoodTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/food-entry")
public class FoodEntryController {

    @Autowired
    private FoodTrackingService foodTrackingService;

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
}
