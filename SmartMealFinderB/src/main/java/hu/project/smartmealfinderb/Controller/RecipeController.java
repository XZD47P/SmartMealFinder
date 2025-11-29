package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
