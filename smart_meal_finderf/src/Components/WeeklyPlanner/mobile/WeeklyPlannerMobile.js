import {useEffect, useState} from "react";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import RestaurantIcon from "@mui/icons-material/Restaurant";
import RecommendationListMobile from "./RecommendationListMobile";
import toast from "react-hot-toast";
import RecipeTileMobile from "./RecipeTileMobile";
import {useMyContext} from "../../../Store/ContextApi";
import api from "../../../Backend/api";
import {searchRecipes} from "../../../Service/recipeService";

const WeeklyPlannerMobile = ({weekPlan, setWeekPlan}) => {
    const [activeDay, setActiveDay] = useState(null);
    const {currentUser} = useMyContext();
    const [recommendations, setRecommendations] = useState({soup: [], main_course: [], snack: [], personal: []});

    // Load recommendations
    useEffect(() => {
        const load = async () => {
            if (!currentUser) return; // Guard clause
            const userDiets = await getUserDiets();
            const userIntolerances = await getUserIntolerances();
            await getRecipeRecommendation(userDiets, userIntolerances);
        };
        load();
    }, [currentUser]);

    const getUserDiets = async () => {
        try {
            const userDiets = await api.get("/diet-option/load-by-user");
            return userDiets.data;
        } catch (error) {
            toast.error("There was an error getting user diets");
            console.error(error); // Log error instead of toast on load to be less intrusive
        }
    };

    const getUserIntolerances = async () => {
        try {
            const userIntolerances = await api.get("/intolerance/load-by-user");
            return userIntolerances.data;
        } catch (error) {
            toast.error("There was an error getting user intolerances");
            console.error(error);
        }
    };

    const getRecipeRecommendation = async (diets, intolerances) => {
        try {
            const offset = Math.floor(Math.random() * 5) * 10;

            const filters = {
                diet: diets?.join(",") || undefined,
                intolerances: intolerances?.join(",") || undefined,
                offset: offset,
            };

            const personalPromise = currentUser?.profilingEnabled
                ? api.get("/recipe/recommendations")
                : Promise.resolve({data: []});

            // Fetching all categories
            const [soup, main_course, snack, personalRes] = await Promise.all([
                searchRecipes({...filters, type: "soup"}),
                searchRecipes({...filters, type: "main course"}),
                searchRecipes({...filters, type: "snack"}),
                personalPromise,
            ]);

            setRecommendations({soup, main_course, snack, personal: personalRes?.data});
        } catch (error) {
            toast.error("Error while trying to retrieve recipes.");
            console.error(error);
        }
    };

    // 1. Remove recipe
    const removeRecipe = (day, recipeId) => {
        setWeekPlan((prev) => ({
            ...prev,
            [day]: prev[day].filter((r) => r.id !== recipeId),
        }));
    };

    // Add Recipe
    const addRecipe = (recipe) => {
        if (!activeDay) return;

        // Limit: Max 3
        if (weekPlan[activeDay].length >= 3) {
            toast.error("Maximum 3 meals allowed per day");
            return;
        }

        // Limit: Duplicates
        if (weekPlan[activeDay].some((r) => r.id === recipe.id)) {
            toast.error("This recipe is already added to this day");
            return;
        }

        setWeekPlan((prev) => ({
            ...prev,
            [activeDay]: [...prev[activeDay], {...recipe}],
        }));

        // Close the selection screen automatically
        setActiveDay(null);
    };

    return (
        <div className="w-full relative bg-gray-50 min-h-[calc(100vh-4rem)]">

            <div className={`p-4 space-y-4 pb-20 ${activeDay ? "hidden" : "block"}`}>
                <div className="flex items-center gap-2 mb-2">
                    <RestaurantIcon className="text-gray-700"/>
                    <h2 className="text-xl font-bold text-gray-800">Your Week</h2>
                </div>

                {Object.entries(weekPlan).map(([day, recipes]) => (
                    <div key={day} className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
                        {/* Day Header */}
                        <div className="flex justify-between items-center mb-3">
                            <h3 className="capitalize font-bold text-lg text-gray-700">{day}</h3>
                            <button
                                onClick={() => setActiveDay(day)}
                                className="text-blue-600 hover:text-blue-800 transition-colors"
                            >
                                <AddCircleOutlineIcon fontSize="medium"/>
                            </button>
                        </div>

                        {/* Planned Meals List */}
                        <div className="p-3 bg-white flex flex-col gap-3 border-t border-gray-100">
                            {recipes.length === 0 && (
                                <p className="text-sm text-gray-400 italic py-1">
                                    No meals planned
                                </p>
                            )}
                            {recipes.map((recipe) => (
                                <RecipeTileMobile
                                    key={recipe.id}
                                    recipe={recipe}
                                    onRemove={() => removeRecipe(day, recipe.id)}
                                />
                            ))}
                        </div>
                    </div>
                ))}
            </div>

            {activeDay && (
                <div className="fixed inset-0 z-50 bg-white flex flex-col animate-in slide-in-from-right duration-200">
                    <div
                        className="p-4 border-b border-gray-200 flex items-center bg-white shadow-sm sticky top-0 z-10">
                        <button
                            onClick={() => setActiveDay(null)}
                            className="mr-3 text-gray-600 p-1 rounded-full hover:bg-gray-100"
                        >
                            <ArrowBackIcon/>
                        </button>
                        <div>
                            <p className="text-xs text-gray-500 uppercase font-bold tracking-wide">Adding meal to</p>
                            <h2 className="text-xl font-bold capitalize text-gray-800">{activeDay}</h2>
                        </div>
                    </div>

                    <div className="flex-1 overflow-y-auto p-4 bg-gray-50">
                        <RecommendationListMobile
                            onRecipeSelect={addRecipe}
                            recipes={recommendations}
                        />
                    </div>
                </div>
            )}
        </div>
    );
};

export default WeeklyPlannerMobile;