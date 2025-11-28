package hu.project.smartmealfinderb.Controller;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
import hu.project.smartmealfinderb.Security.Response.MessageResponse;
import hu.project.smartmealfinderb.Service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/like")
    public ResponseEntity<?> likeRecipe(@RequestBody SpoonacularRecipeResp recipe) {
        this.recipeService.addLiketoRecipe(recipe);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Recipe like saved successfully"));
    }
}
