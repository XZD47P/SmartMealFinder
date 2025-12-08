import {useState} from "react";
import WeeklyPlannerDesktop from "./desktop/WeeklyPlannerDesktop";

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
                {/*<WeeklyPlannerMobile weekplan={weekPlan} setWeekPlan={setWeekPlan}/>*/}
                <span>Mobile not yet implemented</span>
            </div>
        </>
    )

};

export default WeeklyPlannerPage;
