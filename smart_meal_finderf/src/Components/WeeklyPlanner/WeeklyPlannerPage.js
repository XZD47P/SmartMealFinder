import {useEffect, useState} from "react";
import WeeklyPlannerDesktop from "./desktop/WeeklyPlannerDesktop";
import WeeklyPlannerMobile from "./mobile/WeeklyPlannerMobile";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {SaveIcon, ShoppingCart} from "lucide-react";
import {CircularProgress} from "@mui/material";
import {getISOWeekInfo} from "../Utils/DateUtils";
import {useNavigate} from "react-router-dom";

const WeeklyPlannerPage = () => {
    const [loading, setLoading] = useState(false);
    const {year, weekNumber} = getISOWeekInfo(new Date());
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

    useEffect(() => {
        const loadData = async () => {
            const {year, weekNumber} = getISOWeekInfo(new Date());
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
    }, [])

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
        <div className="relative min-h-screen">
            <div className="hidden md:flex">
                <WeeklyPlannerDesktop weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>
            <div className="flex md:hidden">
                <WeeklyPlannerMobile weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>

            {/* Lebegő gombtároló */}
            <div className="fixed bottom-6 right-6 z-50 flex flex-col gap-4 items-end">

                {/* Bevásárlóslista gomb */}
                {weeklyShoppingList.length > 0 && (
                    <button
                        onClick={() => navigate("/shopping-list")}
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
