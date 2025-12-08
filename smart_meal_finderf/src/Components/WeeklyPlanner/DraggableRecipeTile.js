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
        opacity: isDragging ? 0 : 1,
        height: isDragging ? 0 : "auto",      // <-- prevents vertical stretch
        width: isDragging ? 0 : "100%",       // <-- prevents sideways stretch
        pointerEvents: isDragging ? "none" : "auto", // avoids weird pointer issues
    };

    return (
        <div ref={setNodeRef} style={style} {...attributes}>
            <RecipeTile recipe={recipe} showHandle={true} dragHandleProps={listeners}/>
        </div>
    );
};

export default DraggableRecipeTile;