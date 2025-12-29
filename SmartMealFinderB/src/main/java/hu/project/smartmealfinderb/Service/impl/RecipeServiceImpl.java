package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.RecipeTileDTO;
import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.FavouriteRecipe;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.LikedRecipe;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Repository.FavouriteRecipeRepository;
import hu.project.smartmealfinderb.Repository.LikedRecipeRepository;
import hu.project.smartmealfinderb.Service.FoodApiService;
import hu.project.smartmealfinderb.Service.RecipeService;
import hu.project.smartmealfinderb.Service.RecommendationService;
import hu.project.smartmealfinderb.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final LikedRecipeRepository likedRecipeRepository;
    private final UserService userService;
    private final RecommendationService recommendationService;
    private final FavouriteRecipeRepository favouriteRecipeRepository;
    private final FoodApiService foodApiService;

    @Override
    public void addLiketoRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            LikedRecipe likedRecipe = new LikedRecipe();
            likedRecipe.setUser(user);
            likedRecipe.setRecipeId(recipe.getId());

            this.likedRecipeRepository.save(likedRecipe);
            this.recommendationService.sendInteractionToGorse(Interaction.LIKE, user, recipe);
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
            this.recommendationService.deleteInteractionFromGorse(Interaction.LIKE, user, recipe.getId());
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while removing like from recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing like from recipe", e);
        }
    }

    @Override
    public void addFavouriteRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            FavouriteRecipe favouriteRecipe = new FavouriteRecipe(user, recipe.getId());
            this.favouriteRecipeRepository.save(favouriteRecipe);
            this.recommendationService.sendInteractionToGorse(Interaction.FAVOURITE, user, recipe);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while adding bookmark to recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while adding bookmark to recipe", e);
        }
    }

    @Override
    public void removeFavouriteRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            FavouriteRecipe favouriteRecipe = this.favouriteRecipeRepository.findByUserAndRecipeId(user, recipe.getId());
            this.favouriteRecipeRepository.delete(favouriteRecipe);
            this.recommendationService.deleteInteractionFromGorse(Interaction.FAVOURITE, user, recipe.getId());
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while removing bookmark from recipe", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing bookmark from recipe", e);
        }
    }

    @Override
    public List<RecipeTileDTO> getFavouriteRecipes() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            List<FavouriteRecipe> recipeIds = this.favouriteRecipeRepository.findAllByUser(user);

            List<String> stringIds = recipeIds.stream()
                    .map(favouriteRecipe -> String.valueOf(favouriteRecipe.getRecipeId()))
                    .toList();
            return this.foodApiService.getBulkRecipeInfos(stringIds);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while getting favourite recipes", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while getting favourite recipes", e);
        }
    }

    @Override
    public boolean isRecipeFavourite(Long id) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            return this.favouriteRecipeRepository.existsByUserAndRecipeId(user, id);

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error while checking favourite status", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while checking favourite status", e);
        }
    }

    @Override
    public void sendSeenInteractionForRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            this.recommendationService.sendInteractionToGorse(Interaction.SEEN, user, recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending seen interaction", e);
        }
    }

    @Override
    public void sendReadIntercationForRecipe(SpoonacularRecipe recipe) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            this.recommendationService.sendInteractionToGorse(Interaction.READ, user, recipe);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending read interaction", e);
        }
    }

    @Override
    public List<RecipeTileDTO> getRecommendationsForUser() {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            List<String> recipeIds = this.recommendationService.getRecommendations(user);
            return this.foodApiService.getBulkRecipeInfos(recipeIds);

        } catch (Exception e) {
            throw new RuntimeException("Error while getting recommendations from user", e);
        }
    }
}
