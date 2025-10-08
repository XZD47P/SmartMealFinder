package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;

public interface RecipeRecommendationService {
    RemainingDailyMacrosResp calcRemainingMacros();
}
