package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Request.SaveFoodEntryReq;
import hu.project.smartmealfinderb.Model.FoodEntry;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.FoodEntryService;
import hu.project.smartmealfinderb.Service.FoodTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/food-entry")
@RequiredArgsConstructor
public class FoodEntryController {

    private final FoodTrackingService foodTrackingService;
    private final FoodEntryService foodEntryService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFoodEntry(@RequestBody SaveFoodEntryReq newFoodEntry) {

        this.foodTrackingService.saveFoodEntry(newFoodEntry);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Saved Successfully"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getTodayEntries() {

        List<FoodEntry> todayFoodEntryList = this.foodEntryService.findAllTodayEntryByUser();

        return ResponseEntity.status(HttpStatus.OK).body(todayFoodEntryList);
    }

    @DeleteMapping("/delete/{foodEntryId}")
    public ResponseEntity<?> deleteFoodEntry(@PathVariable Long foodEntryId) {

        this.foodTrackingService.deleteFoodEntry(foodEntryId);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Food Entry Deleted Successfully"));
    }
}
