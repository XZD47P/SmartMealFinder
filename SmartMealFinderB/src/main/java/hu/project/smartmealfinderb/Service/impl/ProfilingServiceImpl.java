package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.ProfilingService;
import io.gorse.gorse4j.Feedback;
import io.gorse.gorse4j.Gorse;
import io.gorse.gorse4j.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfilingServiceImpl implements ProfilingService {

    private final Gorse gorseClient;
    private final RestTemplate restTemplate;
    @Value("${gorse.endpoint}")
    private String gorseEndpoint;
    @Value("${gorse.apikey}")
    private String apiKey;

    @Override
    @Async
    public void sendItemToGorse(SpoonacularRecipe recipe) {
        try {
            Item item = new Item(
                    recipe.getId().toString(),
                    false,
                    this.buildLabels(recipe),
                    this.buildCategories(recipe),
                    Instant.now().toString(),
                    recipe.getTitle()
            );

            this.gorseClient.insertItem(item);
        } catch (IOException e) {
            throw new RuntimeException("Error while sending item to gorse", e);
        }
    }

    @Override
    @Async
    public void sendUserToGorse(User user) {
        try {

            io.gorse.gorse4j.User gorseUser = new io.gorse.gorse4j.User(
                    user.getUserId().toString(), List.of()
            );

            this.gorseClient.insertUser(gorseUser);
        } catch (IOException e) {
            throw new RuntimeException("Error while sending user to gorse", e);
        }
    }

    @Override
    @Async
    public void deleteUserFromGorse(User user) {
        try {
            this.gorseClient.deleteUser(user.getUserId().toString());
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting current user from gorse", e);
        }
    }

    @Override
    @Async
    public void sendInteractionToGorse(Interaction interaction, User user, SpoonacularRecipe recipe) {
        //Ez nem lesz Async, mivel osztályon belüli hívás (Self-invocation)
        this.sendItemToGorse(recipe);

        if (user.isProfilingEnabled()) {
            try {
                Feedback feedback = new Feedback(
                        interaction.toString(),
                        user.getUserId().toString(),
                        recipe.getId().toString(),
                        Instant.now().toString()
                );
                this.gorseClient.insertFeedback(List.of(feedback));
            } catch (IOException e) {
                throw new RuntimeException("Error while sending interaction to gorse", e);
            }
        }
    }

    @Override
    @Async
    public void deleteInteractionFromGorse(Interaction interaction, User user, Long recipeId) {
        try {
            String baseUrl = this.gorseEndpoint + "/api/feedback/{feedbackType}/{userId}/{itemId}";

            String requestUrl = UriComponentsBuilder
                    .fromUriString(baseUrl)
                    .buildAndExpand(
                            interaction.toString(),
                            user.getUserId().toString(),
                            recipeId.toString())
                    .toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-Key", this.apiKey);

            this.restTemplate.exchange(
                    requestUrl,
                    HttpMethod.DELETE,
                    new HttpEntity<>(headers),
                    String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting interaction from gorse", e);
        }
    }

    private List<String> buildCategories(SpoonacularRecipe recipe) {
        List<String> categories = new ArrayList<>();

        if (recipe.getDiets() != null) {
            categories.addAll(recipe.getDiets());
        }
        if (recipe.getDishTypes() != null) {
            categories.addAll(recipe.getDishTypes());
        }
        if (recipe.getCuisines() != null) {
            categories.addAll(recipe.getCuisines());
        }
        if (recipe.getOccasions() != null) {
            categories.addAll(recipe.getOccasions());
        }

        return categories;
    }

    private List<String> buildLabels(SpoonacularRecipe recipe) {
        List<String> labels = new ArrayList<>();

        labels.add("healthScore:" + recipe.getHealthScore());
        labels.add("vegeterian:" + recipe.isVegeterian());
        labels.add("vegan:" + recipe.isVegan());
        labels.add("glutenFree:" + recipe.isGlutenFree());
        labels.add("dairyFree:" + recipe.isDairyFree());
        labels.add("veryHealthy:" + recipe.isVeryHealthy());
        labels.add("cheap:" + recipe.isCheap());
        labels.add("veryPopular:" + recipe.isVeryPopular());
        labels.add("sustainable:" + recipe.isSustainable());
        labels.add("lowFodmap:" + recipe.isLowFodmap());
        for (String ingredient : recipe.getIngredientNames()) {
            labels.add("ingredient:" + ingredient);
        }

        return labels;
    }
}
