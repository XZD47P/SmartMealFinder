import {useDroppable} from "@dnd-kit/core";
import DayRecipeTile from "./DayRecipeTile";

const DayColumn = ({day, recipes}) => {
    const {setNodeRef, isOver} = useDroppable({id: day});

    return (
        <div
            ref={setNodeRef}
            className={`p-3 rounded border min-h-[120px] transition
                ${isOver ? "bg-green-100 border-green-400" : "bg-gray-50 border-gray-300"}`}
        >
            <h3 className="font-semibold capitalize mb-2">{day}</h3>

            <div className="flex flex-col gap-2">
                {recipes.map(recipe => (
                    <DayRecipeTile key={recipe.id} recipe={recipe} day={day}/>
                ))}
            </div>
        </div>
    );
};

export default DayColumn;
