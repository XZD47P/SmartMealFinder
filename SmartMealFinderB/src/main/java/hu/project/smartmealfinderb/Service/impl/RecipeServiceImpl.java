package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
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
    public void addLiketoRecipe(SpoonacularRecipeResp recipe) {
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
}
