package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.RecipeRecommendationService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecipeRecommendationController {

    private final UserService userService;
    private final RecipeRecommendationService recipeRecommendationService;

    @GetMapping("/remaining-macros")
    public ResponseEntity<?> calculateRemainingMacros(@AuthenticationPrincipal UserDetails userDetails) {

        User user = this.userService.findByUsername(userDetails.getUsername());
        RemainingDailyMacrosResp resp = this.recipeRecommendationService.calcRemainingMacros(user);

        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }
}
