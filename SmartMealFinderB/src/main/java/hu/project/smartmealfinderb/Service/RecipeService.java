package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;

import java.util.List;

public interface RecipeService {
    void addLiketoRecipe(SpoonacularRecipe recipe);

    boolean isRecipeLiked(Long recipeId);

    int getLikeCount(Long recipeId);

    void removeLikeFromRecipe(SpoonacularRecipe recipe);

    void addFavouriteRecipe(SpoonacularRecipe recipe);

    void removeFavouriteRecipe(SpoonacularRecipe recipe);

    List<Long> getFavouriteRecipeIds();

    boolean isRecipeFavourite(Long id);

    void sendSeenInteractionForRecipe(SpoonacularRecipe recipe);

    void sendReadIntercationForRecipe(SpoonacularRecipe recipe);
}
