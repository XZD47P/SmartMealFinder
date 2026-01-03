package hu.project.smartmealfinderb.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.project.smartmealfinderb.DTO.RecipeTileDTO;
import hu.project.smartmealfinderb.DTO.Response.ShoppingItemDTO;
import hu.project.smartmealfinderb.DTO.WeeklyMealPlanDTO;
import hu.project.smartmealfinderb.Model.User;
import hu.project.smartmealfinderb.Model.WeeklyMealPlan;
import hu.project.smartmealfinderb.Repository.WeeklyMealPlanRepository;
import hu.project.smartmealfinderb.Service.ShoppingListService;
import hu.project.smartmealfinderb.Service.UserService;
import hu.project.smartmealfinderb.Service.WeeklyPlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyPlannerServiceImpl implements WeeklyPlannerService {

    private final WeeklyMealPlanRepository weeklyMealPlanRepository;
    private final UserService userService;
    private final ShoppingListService shoppingListService;
    private final ObjectMapper objectMapper;

    @Override
    public WeeklyMealPlanDTO saveWeeklyMealPlan(WeeklyMealPlanDTO weeklyMealPlanDTO) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();

            this.weeklyMealPlanRepository.deleteByUserAndPlanningYearAndWeekNumber(user,
                    weeklyMealPlanDTO.getYear(),
                    weeklyMealPlanDTO.getWeekNumber());

            Map<String, List<RecipeTileDTO>> weeklyPlanMap = weeklyMealPlanDTO.getPlan();

            List<WeeklyMealPlan> mealsToSave = new ArrayList<>();

            for (Map.Entry<String, List<RecipeTileDTO>> entry : weeklyPlanMap.entrySet()) {
                String dayOfWeek = entry.getKey();
                List<RecipeTileDTO> recipes = entry.getValue();

                if (recipes == null || recipes.isEmpty()) {
                    continue;
                }

                for (RecipeTileDTO recipe : recipes) {

                    String ingredientJson = null;
                    if (recipe.getNutrition() != null && recipe.getNutrition().getIngredients() != null) {
                        try {
                            ingredientJson = this.objectMapper.writeValueAsString(recipe.getNutrition().getIngredients());
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Json Processing error while saving plan: " + e.getMessage(), e);
                        }
                    }

                    double calories = this.getNutrientAmount(recipe, "Calories");
                    double protein = this.getNutrientAmount(recipe, "Protein");
                    double carbs = this.getNutrientAmount(recipe, "Carbohydrates");
                    double fat = this.getNutrientAmount(recipe, "Fat");

                    WeeklyMealPlan meal = new WeeklyMealPlan();
                    meal.setUser(user);
                    meal.setPlanningYear(weeklyMealPlanDTO.getYear());
                    meal.setWeekNumber(weeklyMealPlanDTO.getWeekNumber());
                    meal.setDayOfWeek(dayOfWeek);
                    meal.setRecipeId(recipe.getId());
                    meal.setTitle(recipe.getTitle());
                    meal.setImage(recipe.getImage());
                    meal.setCalories(calories);
                    meal.setProtein(protein);
                    meal.setCarbs(carbs);
                    meal.setFat(fat);
                    meal.setServings(recipe.getServings() != 0 ? recipe.getServings() : 1);

                    meal.setIngredientsJson(ingredientJson);

                    mealsToSave.add(meal);
                }
            }
            List<WeeklyMealPlan> savedMeals = new ArrayList<>();
            if (!mealsToSave.isEmpty()) {
                savedMeals = this.weeklyMealPlanRepository.saveAll(mealsToSave);

            }

            //Mentett terv visszaadása bővítve a bevásárlólistával
            List<ShoppingItemDTO> generatedShoppingList = this.shoppingListService.generateShoppingList(
                    user,
                    weeklyMealPlanDTO.getYear(),
                    weeklyMealPlanDTO.getWeekNumber(),
                    savedMeals
            );

            WeeklyMealPlanDTO response = new WeeklyMealPlanDTO();
            response.setYear(weeklyMealPlanDTO.getYear());
            response.setWeekNumber(weeklyMealPlanDTO.getWeekNumber());

            response.setPlan(weeklyMealPlanDTO.getPlan());

            response.setShoppingList(generatedShoppingList);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error saving weekly meal plan: " + e.getMessage(), e);
        }
    }

    @Override
    public WeeklyMealPlanDTO getWeeklyMealPlan(int year, int week) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();

            List<WeeklyMealPlan> meals = this.weeklyMealPlanRepository.findByUserAndPlanningYearAndWeekNumber(user, year, week);

            Map<String, List<RecipeTileDTO>> planMap = new HashMap<>();
            List<String> days = List.of("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");
            days.forEach(day -> planMap.put(day, new ArrayList<>()));

            for (WeeklyMealPlan meal : meals) {
                RecipeTileDTO dto = new RecipeTileDTO();
                dto.setId(meal.getRecipeId());
                dto.setTitle(meal.getTitle());
                dto.setImage(meal.getImage());
                dto.setServings(meal.getServings());


                RecipeTileDTO.Nutrition nutrition = new RecipeTileDTO.Nutrition();
                List<RecipeTileDTO.Nutrient> nutrientList = new ArrayList<>();

                nutrientList.add(createNutrient("Calories", meal.getCalories(), "kcal"));
                nutrientList.add(createNutrient("Protein", meal.getProtein(), "g"));
                nutrientList.add(createNutrient("Carbohydrates", meal.getCarbs(), "g"));
                nutrientList.add(createNutrient("Fat", meal.getFat(), "g"));

                nutrition.setNutrients(nutrientList);
                if (meal.getIngredientsJson() != null) {
                    try {
                        List<RecipeTileDTO.Ingredient> ingredients = objectMapper.readValue(
                                meal.getIngredientsJson(),
                                //A TypeReference azért kell, hogy a JVM tudja, milyen típusra generálja a kiolvasott értéket
                                new TypeReference<List<RecipeTileDTO.Ingredient>>() {
                                }
                        );
                        nutrition.setIngredients(ingredients);

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Json Processing error getting saved plan: " + e.getMessage(), e);
                    }
                }
                dto.setNutrition(nutrition);


                String day = meal.getDayOfWeek().toLowerCase();
                if (planMap.containsKey(day)) {
                    planMap.get(day).add(dto);
                }

            }

            WeeklyMealPlanDTO response = new WeeklyMealPlanDTO();
            response.setYear(year);
            response.setWeekNumber(week);
            response.setPlan(planMap);
            response.setShoppingList(this.shoppingListService.getShoppingList(user, year, week));

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error getting weekly meal plan: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ShoppingItemDTO> getWeeklyShoppingList(int year, int week) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            return this.shoppingListService.getShoppingList(user, year, week);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void toggleItemBoughtStatus(Long itemId, boolean checked) {
        try {
            User user = this.userService.getCurrentlyLoggedInUser();
            this.shoppingListService.toggleItemBoughtStatus(user, itemId, checked);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating item: " + e.getMessage(), e);
        }
    }

    private RecipeTileDTO.Nutrient createNutrient(String name, double amount, String unit) {
        RecipeTileDTO.Nutrient n = new RecipeTileDTO.Nutrient();
        n.setName(name);
        n.setAmount(amount);
        n.setUnit(unit);
        return n;
    }

    private double getNutrientAmount(RecipeTileDTO recipe, String nutrientName) {
        if (recipe.getNutrition() == null) {
            return 0.0;
        } else {
            return recipe.getNutrition().getNutrients().stream()
                    .filter(nutrient -> nutrient.getName().equalsIgnoreCase(nutrientName))
                    .findFirst()
                    .map(RecipeTileDTO.Nutrient::getAmount)
                    .orElse(0.0);
        }
    }
}
