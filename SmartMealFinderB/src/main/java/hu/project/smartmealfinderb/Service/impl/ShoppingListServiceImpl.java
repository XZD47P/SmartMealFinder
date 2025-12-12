package hu.project.smartmealfinderb.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.project.smartmealfinderb.DTO.RecipeTileDTO;
import hu.project.smartmealfinderb.Model.*;
import hu.project.smartmealfinderb.Repository.ShoppingListRepository;
import hu.project.smartmealfinderb.Service.ShoppingListService;
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
public class ShoppingListServiceImpl implements ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final UnitConverterHelper unitConverterHelper;
    private final ObjectMapper objectMapper;

    @Override
    public void generateShoppingList(User user, int year, int week, List<WeeklyMealPlan> savedPlan) {
        try {
            this.shoppingListRepository.deleteByUserAndPlanningYearAndWeekNumber(user, year, week);

            Map<Long, ShoppingListItem> map = new HashMap<>();

            for (WeeklyMealPlan meal : savedPlan) {
                if (meal.getIngredientsJson() == null) continue;

                try {
                    List<RecipeTileDTO.Ingredient> ingredients = this.objectMapper.readValue(
                            meal.getIngredientsJson(),
                            new TypeReference<List<RecipeTileDTO.Ingredient>>() {
                            }
                    );

                    for (RecipeTileDTO.Ingredient ingredient : ingredients) {
                        this.processIngredient(map, ingredient, meal.getServings());
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error in generating shopping list json: " + e.getMessage(), e);
                }

                ShoppingList shoppingList = new ShoppingList();
                shoppingList.setUser(user);
                shoppingList.setPlanningYear(year);
                shoppingList.setWeekNumber(week);

                List<ShoppingListItem> finalListItems = new ArrayList<>(map.values());
                finalListItems.forEach(shoppingListItem -> shoppingListItem.setShoppingList(shoppingList));

                shoppingList.setItems(finalListItems);

                this.shoppingListRepository.save(shoppingList);
            }
        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating the shopping list: " + e.getMessage(), e);
        }
    }

    private void processIngredient(Map<Long, ShoppingListItem> map, RecipeTileDTO.Ingredient ingredient,
                                   int servings) {
        //Mértékegység típusának meghatározása
        UnitType type = this.unitConverterHelper.determineType(ingredient.getUnit());

        double totalRawAmount = ingredient.getAmount() * servings;

        //Normalizáció
        double normalizedAmount = this.unitConverterHelper.normalize(totalRawAmount, ingredient.getUnit());

        if (map.containsKey(ingredient.getId())) {
            //Összeadás, ha már volt ilyen a listában
            ShoppingListItem existingItem = map.get(ingredient.getId());
            existingItem.setAmount(existingItem.getAmount() + normalizedAmount);
        } else {
            //Új listaelem
            ShoppingListItem item = new ShoppingListItem();
            item.setIngredientId(ingredient.getId());
            item.setName(ingredient.getName());
            item.setAmount(normalizedAmount);
            item.setUnit(type.name());
            item.setChecked(false);

            map.put(ingredient.getId(), item);
        }
    }
}
