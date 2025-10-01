import {Link} from "react-router-dom";

const RecipeTile = ({recipe}) => {
    const nutrients = recipe.nutrition?.nutrients || [];

    const getNutrient = (name) =>
        nutrients.find((nutrient) => nutrient.name.toLowerCase() === name.toLowerCase());

    const calories = getNutrient("Calories");
    const fats = getNutrient("Fat");
    const carbs = getNutrient("Carbohydrates");
    const protein = getNutrient("Protein");


    return (
        <Link to={`/recipes/${recipe.id}`}>
            <div className="border p-4 rounded shadow hover:shadow-lg transition bg-white">
                <img src={recipe.image} alt={recipe.title} className="w-full h-40 object-cover rounded"/>
                <h3 className="text-lg font-semibold mt-2">{recipe.title}</h3>

                {calories && (
                    <div className="text-sm text-gray-600 mt-2 space-y-1">
                        <p>🔥 {Math.round(calories.amount)} {calories.unit}</p>
                        {protein && <p>🍗 {Math.round(protein.amount)}g Protein</p>}
                        {carbs && <p>🌾 {Math.round(carbs.amount)}g Carbs</p>}
                        {fats && <p>🧈 {Math.round(fats.amount)}g Fat</p>}
                    </div>
                )}
            </div>
        </Link>
    );
};

export default RecipeTile;