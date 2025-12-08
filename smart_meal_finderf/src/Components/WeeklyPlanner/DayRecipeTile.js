import {useDraggable} from "@dnd-kit/core";
import {CSS} from "@dnd-kit/utilities";
import RecipeTile from "../Recipe/RecipeTile";


const DayRecipeTile = ({recipe, day}) => {
    const {attributes, listeners, setNodeRef, transform, isDragging} = useDraggable({
        id: `day-${day}-${recipe.id}`,  // <--- unique ID per day
        data: {recipe, sourceDay: day}
    });

    const style = {
        transform: transform ? CSS.Translate.toString(transform) : undefined,
        opacity: isDragging ? 0 : 1,
        height: isDragging ? 0 : "auto",
        width: isDragging ? 0 : "100%",
        pointerEvents: isDragging ? "none" : "auto", // avoids pointer issues
    };
    return (
        <div ref={setNodeRef} style={style} {...attributes} {...listeners}>
            <RecipeTile recipe={recipe} showHandle={true} dragHandleProps={listeners}/>
        </div>
    );
};

export default DayRecipeTile;
