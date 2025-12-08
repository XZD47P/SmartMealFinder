import RecipeTile from "../../Recipe/RecipeTile";
import {DndContext, DragOverlay} from "@dnd-kit/core";
import RecommendationList from "./RecommendationList";
import MealPlanner from "./MealPlanner";
import {useState} from "react";

const WeeklyPlannerDesktop = ({weekPlan, setWeekPlan}) => {


    const [activeRecipe, setActiveRecipe] = useState(null);

    const handleDragStart = (event) => {
        if (event.active?.data?.current?.recipe) {
            setActiveRecipe(event.active.data.current.recipe);
        }
    };

    const handleDragEnd = (event) => {
        const {active, over} = event;
        setActiveRecipe(null);
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
        <DndContext onDragStart={handleDragStart} onDragEnd={handleDragEnd}>
            <div
                className="grid grid-cols-1 lg:grid-cols-2 gap-6 p-4 lg:p-6 h-[calc(100vh-2rem)] w-full overflow-hidden">
                <div className="h-full overflow-y-auto rounded-lg bg-white/50 shadow-sm p-2">
                    <MealPlanner weekPlan={weekPlan}/>
                </div>
                <RecommendationList/>

                <DragOverlay>
                    {activeRecipe ? (
                        <div className="w-64">
                            <RecipeTile recipe={activeRecipe} showHandle={true}/>
                        </div>
                    ) : null}
                </DragOverlay>
            </div>
        </DndContext>
    );
}

export default WeeklyPlannerDesktop;