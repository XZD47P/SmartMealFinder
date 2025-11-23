package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.IngredientInfo;
import hu.project.smartmealfinderb.DTO.Response.ProductInfo;
import hu.project.smartmealfinderb.DTO.Response.RecipeInfo;
import hu.project.smartmealfinderb.DTO.Response.SpoonacularRecipeResp;

import java.util.Map;

public interface FoodApiService {
    Object searchRecipes(Map<String, String> filters);

    Object autoCompleteIngredients(String query);

    ProductInfo getProductInfo(Long id);

    IngredientInfo getIngredientInfo(Long id, double amount, String unit);

    RecipeInfo getRecipeInfo(Long id);

    Object searchProducts(String query);

    Object searchIngredients(String query);

    SpoonacularRecipeResp searchRecipeById(String id);
}
