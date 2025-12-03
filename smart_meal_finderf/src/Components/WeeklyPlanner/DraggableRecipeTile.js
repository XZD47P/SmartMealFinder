import {useDraggable} from "@dnd-kit/core";
import {CSS} from "@dnd-kit/utilities";
import RecipeTile from "../Recipe/RecipeTile";

const DraggableRecipeTile = ({recipe}) => {
    const {attributes, listeners, setNodeRef, transform, isDragging} = useDraggable({
        id: `rec-${recipe.id}`,       // <--- unique ID
        data: {recipe, sourceDay: null}
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

export default DraggableRecipeTile;