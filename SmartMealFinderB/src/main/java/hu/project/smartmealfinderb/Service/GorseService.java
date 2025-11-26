package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;

public interface GorseService {
    void sendItemToGorse(SpoonacularRecipeResp recipe);
}
