import {Link} from "react-router-dom";
import Buttons from "../Utils/Buttons";
import {saveFoodEntry} from "../../Service/recipeService";
import toast from "react-hot-toast";
import {useState} from "react";
import FoodEntryQuantityModal from "../Utils/FoodEntryQuantityModal";
import {useMyContext} from "../../Store/ContextApi";
import {LucideGripVertical} from "lucide-react";

const RecipeTile = ({recipe, button = false, showHandle = false, dragHandleProps}) => {
    const nutrients = recipe.nutrition?.nutrients || [];
    const [open, setOpen] = useState(false);
    const {triggerProgressRefresh} = useMyContext();

    const getNutrient = (name) =>
        nutrients.find((nutrient) => nutrient.name.toLowerCase() === name.toLowerCase());

    const calories = getNutrient("Calories")?.amount;
    const fats = getNutrient("Fat")?.amount;
    const carbs = getNutrient("Carbohydrates")?.amount;
    const protein = getNutrient("Protein")?.amount;

    const handleSave = async (quantity, unit) => {
        try {
            await saveFoodEntry({
                type: "recipe",
                id: recipe.id,
                name: recipe.title,
                calories: calories,
                protein: protein,
                carbs: carbs,
                fats: fats,
                quantity: quantity,
                unit: unit,
            });
            toast.success("Successfully added " + recipe.title + " to your daily intake");
            triggerProgressRefresh();
        } catch (error) {
            toast.error("Error while saving food intake! Please try again later!");
        }
    }
    return (
        <div className="border p-4 rounded shadow hover:shadow-lg transition bg-white">
            <Link to={`/recipes/${recipe.id}`} target={"_blank"}>
                <img src={recipe.image} alt={recipe.title} className="w-full h-40 object-cover rounded"/>
                <h3 className="text-lg font-semibold mt-2">{recipe.title}</h3>

                {calories && (
                    <div className="text-sm text-gray-600 mt-2 space-y-1">
                        <p>🔥 {Math.round(calories)} kcal</p>
                        {protein && <p>🍗 {Math.round(protein)}g Protein</p>}
                        {carbs && <p>🌾 {Math.round(carbs)}g Carbs</p>}
                        {fats && <p>🧈 {Math.round(fats)}g Fat</p>}
                    </div>
                )}
            </Link>
            {button && (
                <Buttons type={"button"} onClickhandler={() => setOpen(true)}
                         className={"bg-green-500 text-white px-3 py-1 mt-2 rounded"}>
                    I ate it!
                </Buttons>
            )}
            {showHandle && dragHandleProps && (
                <div className="px-3 mt-2 text-center">
                    <span {...dragHandleProps} className="cursor-grab inline-block">
                        <LucideGripVertical size={24}/>
                    </span>
                </div>
            )}
            <FoodEntryQuantityModal
                open={open}
                onClose={() => setOpen(false)}
                itemName={recipe?.title}
                itemType={"recipe"}
                onConfirm={({quantity, unit}) => {
                    handleSave(quantity, unit);
                    setOpen(false);
                }}
            />
        </div>
    );
};

export default RecipeTile;