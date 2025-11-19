package hu.project.smartmealfinderb.Service;

import java.util.Map;

public interface FoodApiService {
    Object searchRecipes(Map<String, String> filters);

    Object autoCompleteIngredients(String query);
}
