package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;

public interface RecipeService {
    void addLiketoRecipe(SpoonacularRecipeResp recipe);

    boolean isRecipeLiked(Long recipeId);

    int getLikeCount(Long recipeId);

    void removeLikeFromRecipe(SpoonacularRecipeResp recipe);
}
