package hu.project.smartmealfinderb.Service.impl;

import com.recombee.api_client.RecombeeClient;
import com.recombee.api_client.api_requests.AddItemProperty;
import com.recombee.api_client.api_requests.Batch;
import com.recombee.api_client.api_requests.Request;
import com.recombee.api_client.api_requests.SetItemValues;
import com.recombee.api_client.exceptions.ApiException;
import com.recombee.api_client.exceptions.ResponseException;
import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
import hu.project.smartmealfinderb.Service.RecombeeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecombeeServiceImpl implements RecombeeService {

    private final RecombeeClient client;

    @Override
    @Async
    public void insertOrUpdateRecipe(SpoonacularRecipeResp recipe) {
        try {
            Map<String, Object> properties = this.convertToRecombeeProps(recipe);

            client.send(new SetItemValues(
                            String.valueOf(recipe.getId()),
                            properties
                    ).setCascadeCreate(true)
            );
        } catch (ApiException e) {
            throw new RuntimeException("There was an error while sending the recipe to Recombee: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> convertToRecombeeProps(SpoonacularRecipeResp recipe) {
        return Map.ofEntries(
                Map.entry("title", recipe.getTitle()),
                Map.entry("readyInMinutes", recipe.getReadyInMinutes()),
                Map.entry("healthScore", recipe.getHealthScore()),
                Map.entry("vegetarian", recipe.isVegeterian()),
                Map.entry("vegan", recipe.isVegan()),
                Map.entry("glutenFree", recipe.isGlutenFree()),
                Map.entry("dairyFree", recipe.isDairyFree()),
                Map.entry("veryHealthy", recipe.isVeryHealthy()),
                Map.entry("cheap", recipe.isCheap()),
                Map.entry("veryPopular", recipe.isVeryPopular()),
                Map.entry("sustainable", recipe.isSustainable()),
                Map.entry("lowFodmap", recipe.isLowFodmap()),
                Map.entry("dishTypes", recipe.getDishTypes()),
                Map.entry("diets", recipe.getDiets()),
                Map.entry("cuisines", recipe.getCuisines()),
                Map.entry("occasions", recipe.getOccasions()),
                Map.entry("ingredients", recipe.getIngredientNames())
        );
    }

    @PostConstruct
    private void initializeSchema() {
        try {
            List<Request> schemaRequests = List.of(
                    new AddItemProperty("title", "string"),
                    new AddItemProperty("readyInMinutes", "int"),
                    new AddItemProperty("healthScore", "double"),
                    new AddItemProperty("vegetarian", "boolean"),
                    new AddItemProperty("vegan", "boolean"),
                    new AddItemProperty("glutenFree", "boolean"),
                    new AddItemProperty("dairyFree", "boolean"),
                    new AddItemProperty("veryHealthy", "boolean"),
                    new AddItemProperty("cheap", "boolean"),
                    new AddItemProperty("veryPopular", "boolean"),
                    new AddItemProperty("sustainable", "boolean"),
                    new AddItemProperty("lowFodmap", "boolean"),
                    new AddItemProperty("dishTypes", "set"),
                    new AddItemProperty("diets", "set"),
                    new AddItemProperty("cuisines", "set"),
                    new AddItemProperty("occasions", "set"),
                    new AddItemProperty("ingredients", "set")
            );

            client.send(new Batch(schemaRequests));

        } catch (ResponseException e) {
            if (e.getStatusCode() == 409) {
                System.out.println("The schema already exists in Recombee, new initialization is not required!");
            }
        } catch (ApiException e) {
            throw new RuntimeException("There was an error while initializing the schema", e);
        }
    }
}
