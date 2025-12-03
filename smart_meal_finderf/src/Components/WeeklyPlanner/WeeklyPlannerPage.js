import {useState} from "react";
import {DndContext} from "@dnd-kit/core";
import MealPlanner from "./MealPlanner";
import RecommendationList from "./RecommendationList";

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

    const handleDragEnd = (event) => {
        const {active, over} = event;
        if (!active || !active.data.current) return;

        const recipe = active.data.current.recipe;
        const sourceDay = active.data.current.sourceDay;
        const targetDay = over?.id; // undefined if dropped outside

        // Dragged outside any column → remove from source if exists
        if (!targetDay) {
            if (sourceDay) {
                setWeekPlan(prev => ({
                    ...prev,
                    [sourceDay]: prev[sourceDay].filter(r => r.id !== recipe.id)
                }));
            }
            return;
        }

        // Limit 3 recipes per day
        if (weekPlan[targetDay].some(r => r.id === recipe.id)) return;
        if (weekPlan[targetDay].length >= 3) return;

        setWeekPlan(prev => {
            const newPlan = {...prev};

            // Remove from source if any
            if (sourceDay) {
                newPlan[sourceDay] = newPlan[sourceDay].filter(r => r.id !== recipe.id);
            }

            // Add a clone to target day
            newPlan[targetDay] = [...newPlan[targetDay], {...recipe}];
            return newPlan;
        });
    };

    return (
        <DndContext onDragEnd={handleDragEnd}>
            <div className="grid grid-cols-2 gap-6 p-6 h-full">
                <MealPlanner weekPlan={weekPlan}/>
                <RecommendationList/>
            </div>
        </DndContext>
    );
};

export default WeeklyPlannerPage;
