import api from "../Backend/api";

export const searchRecipes = async (filters = {}) => {
    try {
        const response = await api.get("/food-api/recipes/search", {params: filters})
        return response.data.results;
    } catch (error) {
        console.log("Error seaching recipies: ", error);
    }
};

export const autocompleteIngredients = async (query) => {
    try {
        const response = await api.get("/food-api/ingredients/autocomplete", {params: {query}})
        return response.data; // Egy arrayt ad vissza
    } catch (error) {
        console.error("Error fetching ingredient suggestions:", error);
        return [];
    }
};

export const saveFoodEntry = async (item) => {
    try {
        await api.post("/food-entry/save", item);
    } catch (error) {
        console.error("Error while saving food intake: ", error);
    }
}