import spoonacular from "../Backend/spoonacular";

export const searchRecipes = async (query, filters = {}) => {
    try {
        const response = await spoonacular.get("/recipes/complexSearch", {
            params: {
                query,
                number: 5,
                ...filters,
            },
        });
        return response.data.results;
    } catch (error) {
        console.log("Error seaching recipies: ", error);
    }
};