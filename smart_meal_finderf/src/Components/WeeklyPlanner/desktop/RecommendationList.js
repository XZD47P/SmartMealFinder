import DraggableHorizontalSection from "./DraggableHorizontalSection";
import {useMyContext} from "../../../Store/ContextApi";

const RecommendationList = ({recipes}) => {
    const {currentUser} = useMyContext();

    return (
        <div className="flex flex-col space-y-4 h-full w-full max-w-full overflow-y-auto overflow-x-hidden pr-4">
            <h2 className="text-xl font-bold top-0  z-10 py-2">
                Recommended Recipes
            </h2>
            <div className="grid gap-6 pb-4 w-full">
                {currentUser?.recommendationEnabled && (
                    <DraggableHorizontalSection title={"Recommended based on your activity"}
                                                recipes={recipes.personal}/>
                )}
                <DraggableHorizontalSection title={"🍲 Soup recommendations"} recipes={recipes.soup}/>
                <DraggableHorizontalSection title={"🍽️ Main Course recommendations"} recipes={recipes.main_course}/>
                <DraggableHorizontalSection title={"🍰 Snack recommendations"} recipes={recipes.snack}/>
            </div>
        </div>
    );
};

export default RecommendationList;
