package hu.project.smartmealfinderb.Service.impl;

import hu.project.smartmealfinderb.DTO.Response.IngredientInfo;
import hu.project.smartmealfinderb.DTO.Response.ProductInfo;
import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;
import hu.project.smartmealfinderb.Service.FoodApiService;
import hu.project.smartmealfinderb.Service.ProfilingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FoodApiServiceImpl implements FoodApiService {

    private final RestTemplate restTemplate;
    private final ProfilingService profilingService;
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

    @Override
    public ProductInfo getProductInfo(Long id) {
        try {
            String requestUrl = this.baseUrl + "/food/products/" + id;

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey);

            return this.restTemplate.getForObject(builder.toUriString(), ProductInfo.class);

        } catch (Exception e) {
            throw new RuntimeException("There was an error while getting product info: " + e.getMessage(), e);
        }
    }

    @Override
    public IngredientInfo getIngredientInfo(Long id, double amount, String unit) {
        try {
            String requestUrl = this.baseUrl + "/food/ingredients/" + id + "/information";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("amount", amount)
                    .queryParam("unit", unit);

            return this.restTemplate.getForObject(builder.toUriString(), IngredientInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while getting ingredient info: " + e.getMessage(), e);
        }
    }

    @Override
    public Object searchProducts(String query) {
        try {
            String requestUrl = this.baseUrl + "/food/products/search";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("query", query)
                    .queryParam("number", 5);

            return this.restTemplate.getForObject(builder.toUriString(), Object.class);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for products: " + e.getMessage(), e);
        }

    }

    @Override
    public Object searchIngredients(String query) {
        try {
            String requestUrl = this.baseUrl + "/food/ingredients/search";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("query", query)
                    .queryParam("number", 5);

            return this.restTemplate.getForObject(builder.toUriString(), Object.class);
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for ingredients: " + e.getMessage(), e);
        }
    }

    @Override
    public SpoonacularRecipeResp searchRecipeById(String id) {
        try {
            String requestUrl = this.baseUrl + "/recipes/" + id + "/information";

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(requestUrl)
                    .queryParam("apiKey", this.apikey)
                    .queryParam("includeNutrition", true);

            SpoonacularRecipeResp recipe = this.restTemplate.getForObject(builder.toUriString(), SpoonacularRecipeResp.class);
            this.profilingService.sendItemToGorse(recipe);

            return recipe;
        } catch (Exception e) {
            throw new RuntimeException("There was an error while searching for recipe with id: " + id, e);
        }
    }
}
