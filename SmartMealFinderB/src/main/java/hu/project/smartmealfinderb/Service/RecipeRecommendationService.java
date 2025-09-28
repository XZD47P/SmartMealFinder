package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.RemainingDailyMacrosResp;
import hu.project.smartmealfinderb.Model.User;

public interface RecipeRecommendationService {
    RemainingDailyMacrosResp calcRemainingMacros(User user);
}
