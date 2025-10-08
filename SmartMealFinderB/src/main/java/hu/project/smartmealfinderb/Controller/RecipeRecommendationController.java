package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Service.RecipeRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecipeRecommendationController {

    private final RecipeRecommendationService recipeRecommendationService;

    @GetMapping("/remaining-macros")
    public ResponseEntity<?> calculateRemainingMacros() {

        RemainingDailyMacrosResp resp = this.recipeRecommendationService.calcRemainingMacros();
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
