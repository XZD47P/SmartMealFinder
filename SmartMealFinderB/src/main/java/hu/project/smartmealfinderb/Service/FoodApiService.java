package hu.project.smartmealfinderb.Service;

import hu.project.smartmealfinderb.DTO.Response.IngredientInfo;
import hu.project.smartmealfinderb.DTO.Response.ProductInfo;
import hu.project.smartmealfinderb.DTO.Response.RecipeTileDTO;
import hu.project.smartmealfinderb.DTO.SpoonacularRecipe;

import java.util.List;
import java.util.Map;

public interface FoodApiService {
    Object searchRecipes(Map<String, String> filters);

    Object autoCompleteIngredients(String query);

    ProductInfo getProductInfo(Long id);

    IngredientInfo getIngredientInfo(Long id, double amount, String unit);

    Object searchProducts(String query);

    Object searchIngredients(String query);

    SpoonacularRecipe searchRecipeById(String id);

    List<RecipeTileDTO> getBulkRecipeInfos(List<String> recipeIds);
}
