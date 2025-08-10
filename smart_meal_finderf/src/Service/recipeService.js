import spoonacular from "../Backend/spoonacular";

export const searchRecipes = async (filters = {}) => {
    try {
        const response = await spoonacular.get("/recipes/complexSearch", {
            params: {
                number: 10,
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