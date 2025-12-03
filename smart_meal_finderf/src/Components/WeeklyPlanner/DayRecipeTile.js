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
        opacity: isDragging ? 0.5 : 1
    };

    return (
        <div ref={setNodeRef} style={style} {...attributes} {...listeners}>
            <RecipeTile recipe={recipe} showHandle={true} dragHandleProps={listeners}/>
        </div>
    );
};

export default DayRecipeTile;
