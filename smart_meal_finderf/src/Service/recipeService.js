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

export const findRecipesByIngredients = async (ingredients = []) => {
    try {
        const response = await spoonacular.get("/recipes/findByIngredients", {
            params: {
                ingredients: ingredients.join(","),
                number: 10,
                ranking: 1,  //1: Maximalizálja a használt hozzávalókat;2: Minimalizálja a hiányzókat
                ignorePantry: true, //Hagyja ki ezek közül az átlag dolgokat mint liszt, só, víz stb.
            },
        });
        return response.data;
    } catch (error) {
        console.error("Error searching by fridge ingredients:", error);
        return [];
    }
};