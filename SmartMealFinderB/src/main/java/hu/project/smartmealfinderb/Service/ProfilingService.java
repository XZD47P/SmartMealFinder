package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.User;

public interface ProfilingService {

    void sendItemToGorse(SpoonacularRecipeResp recipe);

    void sendUserToGorse(User user);

    void deleteUserFromGorse(User user);

    void sendInteractionToGorse(Interaction interaction, User user, SpoonacularRecipeResp recipe);
}
