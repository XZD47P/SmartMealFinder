import spoonacular from "../Backend/spoonacular";
import api from "../Backend/api";

export const searchRecipes = async (filters = {}) => {
    try {
        const response = await spoonacular.get("/recipes/complexSearch", {
            params: {
                number: 3,
                ...filters,
            },
        });
        return response.data.results;
    } catch (error) {
        console.log("Error seaching recipies: ", error);
    }
};

export const autocompleteIngredients = async (query) => {
    try {
        const response = await spoonacular.get("/food/ingredients/autocomplete", {
            params: {
                query,
                number: 3,
            },
        });
        return response.data; // Egy arrayt ad vissza
    } catch (error) {
        console.error("Error fetching ingredient suggestions:", error);
        return [];
    }
};

export const saveFoodEntry = async (item) => {
    try {
        let response;
        let spoonacularData;

        switch (item.type) {
            case "product":
                response = await spoonacular.get(`/food/products/${item.id}`);
                spoonacularData = {
                    spoonacularId: item.id,
                    name: item.name,
                    category: item.type,
                    ...extractNutrients(response.data.nutrition),
                }
                break;

            case "ingredient":
                response = await spoonacular.get(`/food/ingredients/${item.id}/information`, {
                    params: {amount: 100, unit: "g"}
                });
                spoonacularData = {
                    spoonacularId: item.id,
                    name: item.name,
                    category: item.type,
                    ...extractNutrients(response.data.nutrition),
                }
                break;

            case "recipe":
                spoonacularData = {
                    spoonacularId: item.id,
                    name: item.name,
                    category: item.type,
                    calories: item.calories,
                    protein: item.protein,
                    carbs: item.carbs,
                    fats: item.fats,
                };
                break;

            default:
                console.error("Unsupported type: ", item.type);
                return;
        }

        await api.post("/food-entry/save", spoonacularData, {
            headers: {
                "Content-Type": "application/json"
            }
        });
    } catch (error) {
        console.error("Error while saving food intake: ", error);
    }
}

const extractNutrients = (nutrition) => {
    if (!nutrition?.nutrients) return {};

    const get = (name) =>
        nutrition.nutrients.find(n => n.name.toLowerCase() === name.toLowerCase())?.amount;

    return {
        calories: get("Calories"),
        protein: get("Protein"),
        carbs: get("Carbohydrates"),
        fats: get("Fat"),
    };
};