package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.RecipeRecommendationService;
import hu.project.smartmealfinderb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/recommendation")
public class RecipeRecommendationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeRecommendationService recipeRecommendationService;

    @GetMapping("/remaining-macros")
    public ResponseEntity<?> calculateRemainingMacros(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = this.userService.findByUsername(userDetails.getUsername());
            RemainingDailyMacrosResp resp = this.recipeRecommendationService.calcRemainingMacros(user);

            return ResponseEntity.status(HttpStatus.OK).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Could not calculate remaining macros"));
        }
    }
}
