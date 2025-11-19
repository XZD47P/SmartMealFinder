package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.Service.FoodApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/food-api")
@RequiredArgsConstructor
public class FoodApiController {

    private final FoodApiService foodApiService;

    @GetMapping("/recipes/search")
    public ResponseEntity<?> searchRecipes(@RequestParam Map<String, String> filters) {
        return ResponseEntity.status(HttpStatus.OK).body(this.foodApiService.searchRecipes(filters));
    }

    @GetMapping("/ingredients/autocomplete")
    public ResponseEntity<?> autoCompleteIngredients(@RequestParam String query) {
        return ResponseEntity.status(HttpStatus.OK).body(this.foodApiService.autoCompleteIngredients(query));
    }
}
