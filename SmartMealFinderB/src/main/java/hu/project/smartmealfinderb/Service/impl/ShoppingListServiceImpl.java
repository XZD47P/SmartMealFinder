package hu.project.smartmealfinderb.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.project.smartmealfinderb.DTO.RecipeTileDTO;
import hu.project.smartmealfinderb.DTO.Response.ShoppingItemDTO;
import hu.project.smartmealfinderb.Model.*;
import hu.project.smartmealfinderb.Repository.ShoppingListItemRepository;
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
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final UnitConverterHelper unitConverterHelper;
    private final ObjectMapper objectMapper;


    @Override
    public List<ShoppingItemDTO> generateShoppingList(User user, int year, int week, List<WeeklyMealPlan> savedPlan) {
        try {
            this.shoppingListRepository.deleteByUserAndPlanningYearAndWeekNumber(user, year, week);

            Map<String, ShoppingListItem> map = new HashMap<>();

            for (WeeklyMealPlan meal : savedPlan) {
                if (meal.getIngredientsJson() == null) continue;

                try {
                    List<RecipeTileDTO.Ingredient> ingredients = this.objectMapper.readValue(
                            meal.getIngredientsJson(),
                            //A TypeReference azért kell, hogy a JVM tudja, milyen típusra generálja a kiolvasott értéket
                            new TypeReference<List<RecipeTileDTO.Ingredient>>() {
                            }
                    );

                    for (RecipeTileDTO.Ingredient ingredient : ingredients) {
                        this.processIngredient(map, ingredient, meal.getServings());
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error in generating shopping list json: " + e.getMessage(), e);
                }
            }

            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setUser(user);
            shoppingList.setPlanningYear(year);
            shoppingList.setWeekNumber(week);

            List<ShoppingListItem> finalListItems = new ArrayList<>(map.values());
            finalListItems.forEach(shoppingListItem -> shoppingListItem.setShoppingList(shoppingList));

            shoppingList.setItems(finalListItems);

            ShoppingList savedList = this.shoppingListRepository.save(shoppingList);
            return mapToDTOs(savedList.getItems());

        } catch (Exception e) {
            throw new RuntimeException("There was an error while generating the shopping list: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ShoppingItemDTO> getShoppingList(User user, int year, int week) {
        try {
            ShoppingList shoppingList = this.shoppingListRepository
                    .findByUserAndPlanningYearAndWeekNumber(user, year, week)
                    .orElse(new ShoppingList());

            return this.mapToDTOs(shoppingList.getItems());
        } catch (Exception e) {
            throw new RuntimeException("There was an error while getting the shopping list: " + e.getMessage(), e);
        }
    }

    @Override
    public void toggleItemBoughtStatus(User user, Long itemId, boolean checked) {
        try {
            ShoppingListItem item = shoppingListItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Shopping list item not found with id: " + itemId));

            Long ownerId = item.getShoppingList().getUser().getUserId();

            if (!ownerId.equals(user.getUserId())) {
                throw new SecurityException("Unauthorized access: You do not own this shopping list item.");
            }

            item.setChecked(checked);
            this.shoppingListItemRepository.save(item);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<ShoppingItemDTO> mapToDTOs(List<ShoppingListItem> items) {
        if (items == null) return new ArrayList<>();

        return items.stream()
                .map(item -> {
                    // Mértékegység meghatározása
                    UnitType type;
                    try {
                        type = UnitType.valueOf(item.getUnit());
                    } catch (IllegalArgumentException | NullPointerException e) {
                        type = UnitType.COUNT;
                    }

                    // Olvasható mértékké konvertálás
                    double displayAmount = this.unitConverterHelper.getDisplayAmount(type, item.getAmount());
                    String displayUnit = this.unitConverterHelper.getDisplayUnit(type, item.getAmount());

                    ShoppingItemDTO shoppingItemDTO = new ShoppingItemDTO();
                    shoppingItemDTO.setId(item.getId());
                    shoppingItemDTO.setIngredientId(item.getIngredientId());
                    shoppingItemDTO.setName(item.getName());
                    shoppingItemDTO.setAmount(displayAmount);
                    shoppingItemDTO.setUnit(displayUnit);
                    shoppingItemDTO.setChecked(item.isChecked());

                    return shoppingItemDTO;
                })
                .toList();
    }

    private void processIngredient(Map<String, ShoppingListItem> map, RecipeTileDTO.Ingredient ingredient,
                                   int servings) {
        if (ingredient.getName().isBlank()) return;

        String key = ingredient.getName().trim().toLowerCase();

        //Mértékegység típusának meghatározása
        UnitType type = this.unitConverterHelper.determineType(ingredient.getUnit());

        double totalRawAmount = ingredient.getAmount() * servings;

        //Normalizáció
        double normalizedAmount = this.unitConverterHelper.normalize(totalRawAmount, ingredient.getUnit());

        if (map.containsKey(key)) {
            //Összeadás, ha már volt ilyen a listában
            ShoppingListItem existingItem = map.get(key);
            existingItem.setAmount(existingItem.getAmount() + normalizedAmount);
        } else {
            //Új listaelem
            ShoppingListItem item = new ShoppingListItem();
            item.setIngredientId(ingredient.getId());
            item.setName(key);
            item.setAmount(normalizedAmount);
            item.setUnit(type.name());
            item.setChecked(false);

            map.put(key, item);
        }
    }
}
