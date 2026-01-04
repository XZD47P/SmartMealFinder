import {useEffect, useState} from "react";
import WeeklyPlannerDesktop from "./desktop/WeeklyPlannerDesktop";
import WeeklyPlannerMobile from "./mobile/WeeklyPlannerMobile";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {SaveIcon, ShoppingCart} from "lucide-react";
import {CircularProgress, useMediaQuery} from "@mui/material";
import {getISOWeekInfo} from "../Utils/DateUtils";
import {useNavigate} from "react-router-dom";
import WeekNavigator from "../Utils/WeekNavigator";
import {useMyContext} from "../../Store/ContextApi";
import {searchRecipes} from "../../Service/recipeService";

const WeeklyPlannerPage = () => {
    const {currentUser} = useMyContext();
    const [loading, setLoading] = useState(false);
    const [referenceDate, setReferenceDate] = useState(new Date());
    const {year, weekNumber} = getISOWeekInfo(referenceDate);
    const [weeklyShoppingList, setWeeklyShoppingList] = useState([]);
    const navigate = useNavigate();
    const [weekPlan, setWeekPlan] = useState({
        monday: [],
        tuesday: [],
        wednesday: [],
        thursday: [],
        friday: [],
        saturday: [],
        sunday: [],
    });
    const isDesktop = useMediaQuery('(min-width: 768px)');
    const [recipes, setRecipes] = useState({personal: [], soup: [], main_course: [], snack: []});

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

            const personalPromise = currentUser?.recommendationEnabled
                ? api.get("/recipe/recommendations")
                : Promise.resolve({data: []});

            // Fetching all categories
            const [personalRes, soup, main_course, snack] = await Promise.all([
                personalPromise,
                searchRecipes({...filters, type: "soup"}),
                searchRecipes({...filters, type: "main course"}),
                searchRecipes({...filters, type: "snack"}),
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
    const handlePrevWeek = () => {
        const newDate = new Date(referenceDate);
        newDate.setDate(newDate.getDate() - 7); // Subtract 7 days
        setReferenceDate(newDate);
    };

    const handleNextWeek = () => {
        const newDate = new Date(referenceDate);
        newDate.setDate(newDate.getDate() + 7); // Add 7 days
        setReferenceDate(newDate);
    };

    const handleJumpToToday = () => {
        setReferenceDate(new Date());
    };

    useEffect(() => {
        const loadData = async () => {
            try {
                const response = await api.get("/weekly-planner/load",
                    {params: {year: year, week: weekNumber}})

                if (response.data) {
                    if (response.data.plan) {
                        setWeekPlan(response.data.plan);
                    }

                    if (response.data.shoppingList) {
                        setWeeklyShoppingList(response.data.shoppingList);
                    }
                }
            } catch (error) {
                console.error("Failed to load data", error);
            }
        }
        loadData();
    }, [year, weekNumber])

    const sendWeeklyPlan = async () => {
        try {
            setLoading(true);
            const response = await api.post("/weekly-planner/save", {
                year: year,
                weekNumber: weekNumber,
                plan: weekPlan,
            });

            if (response.data) {
                if (response.data.plan) {
                    setWeekPlan(response.data.plan);
                }

                if (response.data.shoppingList) {
                    setWeeklyShoppingList(response.data.shoppingList);
                }
            }
            toast.success("Weekly meal plan saved");
            setLoading(false);
        } catch (error) {
            toast.error("There was an error while sending weekly plan");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="relative min-h-screen bg-gray-50 flex flex-col">
            <div className="pt-4 md:pt-6 md:px-6">
                <WeekNavigator
                    currentYear={year}
                    currentWeek={weekNumber}
                    onPrev={handlePrevWeek}
                    onNext={handleNextWeek}
                    onToday={handleJumpToToday}
                />
            </div>

            <div className="flex-1 relative">
                {loading && (
                    <div
                        className="absolute inset-0 bg-white/50 z-40 flex items-center justify-center backdrop-blur-sm transition-all">
                        <CircularProgress/>
                    </div>
                )}

                {isDesktop ? (
                    <WeeklyPlannerDesktop
                        weekPlan={weekPlan}
                        setWeekPlan={setWeekPlan}
                        recommendations={recipes}/>
                ) : (
                    <WeeklyPlannerMobile
                        weekPlan={weekPlan}
                        setWeekPlan={setWeekPlan}
                        recommendations={recipes}/>
                )}
            </div>

            {/* Lebegő gombtároló */}
            <div className="fixed bottom-6 right-6 z-50 flex flex-col gap-4 items-end">

                {/* Bevásárlóslista gomb */}
                {weeklyShoppingList.length > 0 && (
                    <button
                        onClick={() => navigate("/shopping-list", {state: {list: weeklyShoppingList}})}
                        className="
                        flex items-center gap-2
                        bg-green-600 hover:bg-green-700 active:scale-95
                        text-white font-semibold
                        shadow-lg hover:shadow-xl hover:shadow-green-600/30
                        transition-all duration-200 ease-in-out
                        rounded-full px-4 py-3 md:px-6 md:py-3
                        animate-in slide-in-from-bottom-4 fade-in duration-300"
                    >
                        <ShoppingCart className="w-5 h-5"/>
                        <span className="hidden sm:inline">
                            Shopping List ({weeklyShoppingList.length})
                        </span>
                    </button>
                )}

                {/*Mentés gomb*/}
                <button
                    onClick={sendWeeklyPlan}
                    disabled={loading}
                    className="
                    flex items-center gap-2
                    bg-blue-600 hover:bg-blue-700 active:scale-95
                    text-white font-semibold
                    shadow-lg hover:shadow-xl hover:shadow-blue-600/30
                    transition-all duration-200 ease-in-out
                    rounded-full px-4 py-3 md:px-6 md:py-3
                    disabled:opacity-70 disabled:cursor-not-allowed"
                >
                    {loading ? (
                        <CircularProgress size={24} color="inherit"/>
                    ) : (
                        <>
                            <SaveIcon className="w-5 h-5"/>
                            <span className="hidden sm:inline">Save Plan</span>
                        </>
                    )}
                </button>
            </div>
        </div>
    )

};

export default WeeklyPlannerPage;
