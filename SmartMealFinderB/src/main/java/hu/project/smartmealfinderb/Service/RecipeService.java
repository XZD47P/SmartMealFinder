package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;

public interface RecipeService {
    void addLiketoRecipe(SpoonacularRecipeResp recipe);
}
