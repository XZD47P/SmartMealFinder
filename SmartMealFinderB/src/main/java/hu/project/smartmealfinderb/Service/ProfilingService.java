package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.User;

public interface ProfilingService {

    void sendItemToGorse(SpoonacularRecipe recipe);

    void sendUserToGorse(User user);

    void deleteUserFromGorse(User user);

    void sendInteractionToGorse(Interaction interaction, User user, SpoonacularRecipe recipeId);

    void deleteInteractionFromGorse(Interaction interaction, User user, Long recipeId);
}
