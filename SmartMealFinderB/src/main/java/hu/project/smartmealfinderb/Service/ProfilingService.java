package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
import hu.project.smartmealfinderb.Model.User;
import org.springframework.scheduling.annotation.Async;

public interface ProfilingService {
    @Async
    void sendItemToGorse(SpoonacularRecipeResp recipe);

    @Async
    void sendUserToGorse(User user);

    @Async
    void deleteUserFromGorse(User user);
}
