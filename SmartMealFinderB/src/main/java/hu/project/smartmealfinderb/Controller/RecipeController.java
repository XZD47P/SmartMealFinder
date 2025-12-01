package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/like")
    public ResponseEntity<?> likeRecipe(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.addLiketoRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Recipe like saved successfully"));
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeRecipe(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.removeLikeFromRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Recipe like removed"));
    }

    @GetMapping("/isLiked")
    public ResponseEntity<?> isRecipeLiked(@RequestParam Long id) {
        boolean recipeLiked = this.recipeService.isRecipeLiked(id);
        return ResponseEntity.status(HttpStatus.OK).body(recipeLiked);
    }

    @GetMapping("/likeCount")
    public ResponseEntity<?> getLikeCount(@RequestParam Long id) {
        int count = this.recipeService.getLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @PostMapping("/favourite/add")
    public ResponseEntity<?> addFavouriteToRecipe(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.addFavouriteRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Favourite recipe added successfully"));
    }

    @DeleteMapping("/favourite/remove")
    public ResponseEntity<?> removeFavouriteFromRecipe(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.removeFavouriteRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Favourite recipe removed successfully"));
    }

    @GetMapping("/favourite")
    public ResponseEntity<?> getFavouriteRecipeIds() {
        List<Long> favouriteRecipeIds = this.recipeService.getFavouriteRecipeIds();
        return ResponseEntity.status(HttpStatus.OK).body(favouriteRecipeIds);
    }

    @GetMapping("/isFavourite")
    public ResponseEntity<?> isFavouriteRecipeId(@RequestParam Long id) {
        boolean favouritedRecipe = this.recipeService.isRecipeFavourite(id);
        return ResponseEntity.status(HttpStatus.OK).body(favouritedRecipe);
    }

    @PostMapping("/seen")
    public ResponseEntity<?> sendRecipeSeen(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.sendSeenInteractionForRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Recipe status set seen"));
    }

    @PostMapping("/read")
    public ResponseEntity<?> sendRecipeRead(@RequestBody SpoonacularRecipe recipe) {
        this.recipeService.sendReadIntercationForRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Recipe status set read"));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecipeRecommendations() {
        List<String> recommendations = this.recipeService.getRecommendationsForUser();
        return ResponseEntity.status(HttpStatus.OK).body(recommendations);
    }
}
