package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.LikedRecipe;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.LikedRecipeRepository;
import hu.project.smartmealfinderb.Service.ProfilingService;
import hu.project.smartmealfinderb.Service.RecipeService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final LikedRecipeRepository likedRecipeRepository;
    private final UserService userService;
    private final ProfilingService profilingService;

    @Override
    public void addLiketoRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            LikedRecipe likedRecipe = new LikedRecipe();
            likedRecipe.setUser(user);
            likedRecipe.setRecipeId(recipe.getId());

            this.likedRecipeRepository.save(likedRecipe);
            this.profilingService.sendInteractionToGorse(Interaction.LIKE, user, recipe);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while adding like to recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding like to recipe", e);
        }
    }

    @Override
    public boolean isRecipeLiked(Long recipeId) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            return this.likedRecipeRepository.existsByUserAndRecipeId(user, recipeId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while checking if recipe is liked by user", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while checking if recipe is liked by user", e);
        }
    }

    @Override
    public int getLikeCount(Long recipeId) {
        try {
            return this.likedRecipeRepository.countByRecipeId(recipeId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while checking like numbers for recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while checking like numbers for recipe", e);
        }
    }

    @Override
    public void removeLikeFromRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            LikedRecipe likedRecipe = this.likedRecipeRepository.getLikedRecipeByUserAndRecipeId(user, recipe.getId());
            this.likedRecipeRepository.delete(likedRecipe);
            this.profilingService.deleteInteractionFromGorse(Interaction.LIKE, user, recipe.getId());
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while removing like from recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing like from recipe", e);
        }
    }
}
