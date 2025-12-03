import DayColumn from "./DayColumn";

const days = ["monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"];

const MealPlanner = ({weekPlan}) => {
    return (
        <div className="space-y-4">
            <h2 className="text-xl font-bold">Weekly Plan</h2>

            <div className="space-y-4">
                {days.map(day => (
                    <DayColumn key={day} day={day} recipes={weekPlan[day]}/>
                ))}
            </div>
        </div>
    );
};

export default MealPlanner;