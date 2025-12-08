import {Link} from "react-router-dom";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import AgricultureIcon from '@mui/icons-material/Agriculture';
import WaterDropIcon from '@mui/icons-material/WaterDrop';
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";

const RecipeTileMobile = ({recipe, onAdd, onRemove}) => {
    const nutrients = recipe.nutrition?.nutrients || [];
    const getNutrient = (name) =>
        nutrients.find((nutrient) => nutrient.name.toLowerCase() === name.toLowerCase());

    const calories = getNutrient("Calories")?.amount;
    const fats = getNutrient("Fat")?.amount;
    const carbs = getNutrient("Carbohydrates")?.amount;
    const protein = getNutrient("Protein")?.amount;

    return (
        <div
            className="flex items-center justify-between bg-white p-3 rounded-xl shadow-sm border border-gray-100 mb-3 active:scale-[0.98] transition-transform">

            <Link
                to={`/recipes/${recipe.id}`}
                target="_blank"
                className="flex items-center flex-1 min-w-0 mr-3"
            >
                <img
                    src={recipe.image}
                    alt={recipe.title}
                    className="w-16 h-16 object-cover rounded-lg bg-gray-200"
                />

                <div className="ml-3 overflow-hidden">
                    <h4 className="text-sm font-bold text-gray-800 truncate">
                        {recipe.title}
                    </h4>
                    <div className="flex flex-wrap items-center gap-3 mt-2">
                        {calories && (
                            <span
                                className="text-xs text-orange-500 font-bold flex items-center bg-orange-50 px-1.5 py-0.5 rounded">
                                <LocalFireDepartmentIcon fontSize="inherit" className="mr-1"/>
                                {Math.round(calories)} kcal Calories
                            </span>
                        )}
                        {protein && (
                            <span
                                className="text-xs text-blue-600 font-medium flex items-center bg-blue-50 px-1.5 py-0.5 rounded">
                                <FitnessCenterIcon fontSize="inherit" className="mr-1"/>
                                {Math.round(protein)}g Protein
                            </span>
                        )}
                        {carbs && (
                            <span
                                className="text-xs text-green-600 font-medium flex items-center bg-green-50 px-1.5 py-0.5 rounded">
                                <AgricultureIcon fontSize="inherit" className="mr-1"/>
                                {Math.round(carbs)}g Carbs
                             </span>
                        )}
                        {fats && (
                            <span
                                className="text-xs text-yellow-600 font-medium flex items-center bg-yellow-50 px-1.5 py-0.5 rounded">
                                <WaterDropIcon fontSize="inherit" className="mr-1"/>
                                {Math.round(fats)}g Fat
                             </span>
                        )}
                    </div>
                    <span className="text-xs text-blue-500 underline mt-1 block">
                        View Details
                    </span>
                </div>
            </Link>

            {/* e.stopPropagation() prevents the Link from triggering when clicking the button */}
            <button
                onClick={(e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    if (onRemove) onRemove(recipe.id);
                    if (onAdd) onAdd(recipe);
                }}
                className={`p-2 -mr-2 transition-colors ${
                    onRemove
                        ? "text-gray-400 hover:text-red-500" // Red for Remove
                        : "text-blue-600 hover:text-blue-800" // Blue for Add
                }`}
                aria-label={onRemove ? "Remove from plan" : "Add to plan"}
            >
                {onRemove ? (
                    <RemoveCircleOutlineIcon sx={{fontSize: 32}}/>
                ) : (
                    <AddCircleOutlineIcon sx={{fontSize: 32}}/>
                )}
            </button>
        </div>
    );
};

export default RecipeTileMobile;