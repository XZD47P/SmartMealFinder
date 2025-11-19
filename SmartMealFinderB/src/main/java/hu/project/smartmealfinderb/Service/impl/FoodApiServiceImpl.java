package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.Service.FoodApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class FoodApiServiceImpl implements FoodApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${spoonacular.apiKey}")
    private String apikey;
    @Value("${spoonacular.baseUrl}")
    private String baseUrl;

    @Override
    public Object searchRecipes(Map<String, String> filters) {
        try {
            String requestUrl = this.baseUrl + "/recipes/complexSearch";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("addRecipeNutrition", true)
                    .queryParam("number", 3);

            //builder::queryParam=(key, value) -> builder.queryParam(key, value)
            filters.forEach(builder::queryParam);
            ResponseEntity<Object> response = this.restTemplate.getForEntity(builder.toUriString(), Object.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for recipes: " + e.getMessage(), e);
        }
    }

    @Override
    public Object autoCompleteIngredients(String query) {
        try {
            String requestUrl = this.baseUrl + "/food/ingredients/autocomplete";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("query", query)
                    .queryParam("number", 3);


            ResponseEntity<Object> response = this.restTemplate.getForEntity(builder.toUriString(), Object.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for ingredients: " + e.getMessage(), e);
        }
    }
}
