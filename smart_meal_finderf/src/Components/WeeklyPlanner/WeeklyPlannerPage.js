import {useState} from "react";
import WeeklyPlannerDesktop from "./desktop/WeeklyPlannerDesktop";
import WeeklyPlannerMobile from "./mobile/WeeklyPlannerMobile";

const WeeklyPlannerPage = () => {
    const [weekPlan, setWeekPlan] = useState({
        monday: [],
        tuesday: [],
        wednesday: [],
        thursday: [],
        friday: [],
        saturday: [],
        sunday: [],
    });

    return (
        <>
            <div className="hidden md:flex">
                <WeeklyPlannerDesktop weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>
            <div className="flex md:hidden">
                <WeeklyPlannerMobile weekPlan={weekPlan} setWeekPlan={setWeekPlan}/>
            </div>
        </>
    )

};

export default WeeklyPlannerPage;
