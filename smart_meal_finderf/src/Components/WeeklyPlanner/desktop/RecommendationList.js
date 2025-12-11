import DraggableHorizontalSection from "./DraggableHorizontalSection";
import {useEffect, useState} from "react";
import {searchRecipes} from "../../../Service/recipeService";
import toast from "react-hot-toast";
import {useMyContext} from "../../../Store/ContextApi";
import api from "../../../Backend/api";

const RecommendationList = () => {
    const [recipes, setRecipes] = useState({personal: [], soup: [], main_course: [], snack: []});
    const {currentUser} = useMyContext();

    useEffect(() => {
        const load = async () => {
            const userDiets = await getUserDiets();
            const userIntolerances = await getUserIntolerances();
            await getRecipeRecommendation(userDiets, userIntolerances);
        };

        load();
    }, [currentUser]);

    const getRecipeRecommendation = async (diets, intolerances) => {
        try {
            const offset = Math.floor(Math.random() * 5) * 10; //Offset, hogy más recepteket jelenítsen meg

            const filters = {
                diet: diets?.join(",") || undefined,
                intolerances: intolerances?.join(",") || undefined,
                offset: offset,
            };

            const personalPromise = currentUser?.profilingEnabled
                ? api.get("/recipe/recommendations")
                : Promise.resolve({data: []});

            // Fetching all categories
            const [personalRes, soup, main_course, snack] = await Promise.all([
                personalPromise,
                searchRecipes({...filters, type: "soup"}),
                // searchRecipes({...filters, type: "main course"}),
                // searchRecipes({...filters, type: "snack"}),
            ]);

            setRecipes({personal: personalRes?.data, soup, main_course, snack});
        } catch (error) {
            toast.error("Error while trying to retrieve recipes.");
        }
    }

    const getUserDiets = async () => {
        try {
            const userDiets = await api.get("/diet-option/load-by-user");
            return userDiets.data;
        } catch (error) {
            toast.error("Error while trying to retrieve user diets.");
        }
    }

    const getUserIntolerances = async () => {
        try {
            const userIntolerances = await api.get("/intolerance/load-by-user");
            return userIntolerances.data;
        } catch (error) {
            toast.error("Error while trying to retrieve user intolerances.");
        }
    }

    return (
        <div className="flex flex-col space-y-4 h-full overflow-y-auto overflow-x-hidden pr-2">
            <h2 className="text-xl font-bold top-0  z-10 py-2">
                Recommended Recipes
            </h2>
            <div className="grid gap-6 pb-4">
                {currentUser?.profilingEnabled && (
                    <DraggableHorizontalSection title={"Recommended based on your activity"}
                                                recipes={recipes.personal}/>
                )}
                <DraggableHorizontalSection title={"🍲 Soup recommendations"} recipes={recipes.soup}/>
                <DraggableHorizontalSection title={"🍽️ Main Course recommendations"} recipes={recipes.main_course}/>
                <DraggableHorizontalSection title={"🍰 Snack recommendations"} recipes={recipes.snack}/>
            </div>
        </div>
    );
};

export default RecommendationList;
