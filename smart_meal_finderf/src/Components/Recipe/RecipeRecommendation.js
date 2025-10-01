import {useEffect, useState} from "react";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import {searchRecipes} from "../../Service/recipeService";
import HorizontalSection from "../Utils/HorizontalSection";


const RecipeRecommendation = ({dietPlan}) => {
    const [recipes, setRecipes] = useState({breakfast: [], main_course: [], snack: []});
    const [loading, setLoading] = useState(false);
    const [macros, setMacros] = useState(null);


    useEffect(() => {
        if (dietPlan) {
            fetchRecommendations();
        }
    }, [dietPlan]);

    useEffect(() => {
        if (macros) {
            const getRecipeRecommendation = async () => {
                try {
                    setLoading(true);
                    const offset = Math.floor(Math.random() * 5) * 10; //Offset, hogy más recepteket jelenítsen meg

                    const filters = {
                        diet: macros.diets?.join(",") || undefined,
                        intolerances: macros.intolerances?.join(",") || undefined,
                        maxCalories: macros.remainingCalories,
                        maxProtein: macros.remainingProteins,
                        maxCarbs: macros.remainingCarbs,
                        maxFat: macros.remainingFats,
                        //addRecipeNutrition: true,
                        offset: offset,
                    }
                    const [breakfast, main_course, snack] = await Promise.all([
                        searchRecipes({...filters, type: "breakfast"}),
                        searchRecipes({...filters, type: "main course"}),
                        searchRecipes({...filters, type: "snack"}),
                    ]);

                    setRecipes({breakfast, main_course, snack});
                } catch (err) {
                    toast.error("Error while trying to retrieve recipes.");
                } finally {
                    setLoading(false);
                }
            }

            getRecipeRecommendation();
        }
    }, [macros]);

    const fetchRecommendations = async () => {
        try {
            setLoading(true);
            const response = await api.get("/recommendation/remaining-macros");
            setMacros(response.data);
        } catch (err) {
            toast.error("Error while fetching data for recommendations.");
        } finally {
            setLoading(false);
        }
    }


    return (
        <div className="space-y-8">
            <HorizontalSection title="🍳 Breakfast Recommendations" recipes={recipes.breakfast}/>
            <HorizontalSection title="🥗 Main Course Recommendations" recipes={recipes.main_course}/>
            <HorizontalSection title="🍫 Snack Recommendations" recipes={recipes.snack}/>
        </div>
    );
};

export default RecipeRecommendation;