package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;

public interface RecombeeService {
    void insertOrUpdateRecipe(SpoonacularRecipeResp recipe);
}
