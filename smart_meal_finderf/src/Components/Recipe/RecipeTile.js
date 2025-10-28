import {Link} from "react-router-dom";
import Buttons from "../Utils/Buttons";
import {saveFoodEntry} from "../../Service/recipeService";
import toast from "react-hot-toast";
import {useState} from "react";
import FoodEntryQuantityModal from "../Utils/FoodEntryQuantityModal";

const RecipeTile = ({recipe, button = false}) => {
    const nutrients = recipe.nutrition?.nutrients || [];
    const [open, setOpen] = useState(false);

    const getNutrient = (name) =>
        nutrients.find((nutrient) => nutrient.name.toLowerCase() === name.toLowerCase());

    const calories = Math.round(getNutrient("Calories")?.amount);
    const fats = Math.round(getNutrient("Fat")?.amount);
    const carbs = Math.round(getNutrient("Carbohydrates")?.amount);
    const protein = Math.round(getNutrient("Protein")?.amount);

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
        } catch (error) {
            toast.error("Error while saving food intake! Please try again later!");
        }
    }
    return (
        <div className="border p-4 rounded shadow hover:shadow-lg transition bg-white">
            <Link to={`/recipes/${recipe.id}`}>
                <img src={recipe.image} alt={recipe.title} className="w-full h-40 object-cover rounded"/>
                <h3 className="text-lg font-semibold mt-2">{recipe.title}</h3>

                {calories && (
                    <div className="text-sm text-gray-600 mt-2 space-y-1">
                        <p>🔥 {calories} kcal</p>
                        {protein && <p>🍗 {protein}g Protein</p>}
                        {carbs && <p>🌾 {carbs}g Carbs</p>}
                        {fats && <p>🧈 {fats}g Fat</p>}
                    </div>
                )}
            </Link>
            {button && (
                <Buttons type={"button"} onClickhandler={() => setOpen(true)}
                         className={"bg-green-500 text-white px-3 py-1 mt-2 rounded"}>
                    I ate it!
                </Buttons>
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