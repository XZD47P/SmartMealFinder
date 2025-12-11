import {useState} from "react";
import WeeklyPlannerDesktop from "./desktop/WeeklyPlannerDesktop";
import WeeklyPlannerMobile from "./mobile/WeeklyPlannerMobile";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {SaveIcon} from "lucide-react";
import {CircularProgress} from "@mui/material";
import {getISOWeekInfo} from "../Utils/DateUtils";

const WeeklyPlannerPage = () => {
    const [loading, setLoading] = useState(false);
    const {year, weekNumber} = getISOWeekInfo(new Date());
    const [weekPlan, setWeekPlan] = useState({
        monday: [],
        tuesday: [],
        wednesday: [],
        thursday: [],
        friday: [],
        saturday: [],
        sunday: [],
    });

    const sendWeeklyPlan = async () => {
        try {
            setLoading(true);
            await api.post("/weekly-planner/save", {
                year: year,
                weekNumber: weekNumber,
                plan: weekPlan,
            });
            toast.success("Weekly meal plan saved");
            setLoading(false);
        } catch (error) {
            toast.error("There was an error while sending weekly plan");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div className="relative win-h-screen">
            <div className="hidden md:flex">
                <WeeklyPlannerDesktop weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>
            <div className="flex md:hidden">
                <WeeklyPlannerMobile weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>

            <button onClick={sendWeeklyPlan}
                    disabled={loading}
                    className="
                    fixed bottom-6 right-6 z-50
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
                        <SaveIcon/>
                        <span className="hidden sm:inline">Save Plan</span>
                    </>
                )}
            </button>
        </div>
    )

};

export default WeeklyPlannerPage;
