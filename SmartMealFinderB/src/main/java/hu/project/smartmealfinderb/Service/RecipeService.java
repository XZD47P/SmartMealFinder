package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;

public interface RecipeService {
    void addLiketoRecipe(SpoonacularRecipe recipe);

    boolean isRecipeLiked(Long recipeId);

    int getLikeCount(Long recipeId);

    void removeLikeFromRecipe(SpoonacularRecipe recipe);
}
