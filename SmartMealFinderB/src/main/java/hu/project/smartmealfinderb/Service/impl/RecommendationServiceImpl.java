package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;
import hu.project.smartmealfinderb.Model.Interaction;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Service.RecommendationService;
import io.gorse.gorse4j.Feedback;
import io.gorse.gorse4j.Gorse;
import io.gorse.gorse4j.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

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

        if (user.isRecommendationEnabled()) {
            try {
                Feedback feedback = new Feedback(
                        interaction.getType(),
                        user.getUserId().toString(),
                        recipe.getId().toString(),
                        interaction.getWeight(),
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
            if (user.isRecommendationEnabled()) {
                this.gorseClient.deleteFeedback(interaction.getType(), user.getUserId().toString(), recipeId.toString());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting interaction from gorse", e);
        }
    }

    @Override
    public List<String> getRecommendations(User user) {
        try {

            if (!user.isRecommendationEnabled()) {
                throw new RuntimeException("User recommendation is disabled");
            }
            return this.gorseClient.getRecommend(user.getUserId().toString());
        } catch (IOException e) {
            throw new RuntimeException("Error while getting recommendations from gorse: " + e.getMessage(), e);
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

        labels.add(this.getHealthBucket(recipe.getHealthScore()));
        if (recipe.isVegeterian()) labels.add("vegetarian");
        if (recipe.isVegan()) labels.add("vegan");
        if (recipe.isGlutenFree()) labels.add("gluten_free");
        if (recipe.isDairyFree()) labels.add("dairy_free");
        if (recipe.isCheap()) labels.add("cheap");
        if (recipe.isVeryPopular()) labels.add("very_popular");
        if (recipe.isSustainable()) labels.add("sustainable");
        if (recipe.isLowFodmap()) labels.add("low_fodmap");
        for (String ingredient : recipe.getIngredientNames()) {
            labels.add("ingredient:" + ingredient);
        }

        return labels;
    }

    private String getHealthBucket(double healthScore) {
        if (healthScore >= 85) {
            return "healthiness:very_high";
        } else if (healthScore >= 70) {
            return "healthiness:high";
        } else if (healthScore >= 55) {
            return "healthiness:medium";
        } else {
            return "healthiness:unhealthy";
        }
    }
}
