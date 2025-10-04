import {Link} from "react-router-dom";
import Buttons from "../Utils/Buttons";
import {saveFoodEntry} from "../../Service/recipeService";

const RecipeTile = ({recipe, button = false}) => {
    const nutrients = recipe.nutrition?.nutrients || [];

    const getNutrient = (name) =>
        nutrients.find((nutrient) => nutrient.name.toLowerCase() === name.toLowerCase());

    const calories = Math.round(getNutrient("Calories")?.amount);
    const fats = Math.round(getNutrient("Fat")?.amount);
    const carbs = Math.round(getNutrient("Carbohydrates")?.amount);
    const protein = Math.round(getNutrient("Protein")?.amount);

    const handleSave = async () => {
        await saveFoodEntry({
            type: "recipe",
            id: recipe.id,
            name: recipe.title,
            calories: calories,
            protein: protein,
            carbs: carbs,
            fats: fats,
        });
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
                <Buttons type={"button"} onClickhandler={handleSave}
                         className={"bg-green-500 text-white px-3 py-1 mt-2 rounded"}>
                    I ate it!
                </Buttons>
            )}
        </div>
    );
};

export default RecipeTile;