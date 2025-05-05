import {useState} from "react";
import {findRecipesByIngredients} from "../Service/recipeService";
import RecipeTile from "./Recipe/RecipeTile";
import Select from "react-select";

const ingredientOptions = [
    {value: "egg", label: "Egg"},
    {value: "milk", label: "Milk"},
    {value: "cheese", label: "Cheese"},
    {value: "tomato", label: "Tomato"},
    {value: "chicken", label: "Chicken"},
    {value: "onion", label: "Onion"},
    {value: "flour", label: "Flour"},
    {value: "butter", label: "Butter"},
];

const WhatsInMyFridgePage = () => {
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [recipes, setRecipes] = useState([]);

    const handleSearch = async () => {
        const ingridients = selectedIngredients.map((ingredient) => ingredient.value);
        const data = await findRecipesByIngredients(ingridients);
        setRecipes(data)
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">What's in Your Fridge?</h1>

            <div className="mb-4">
                <label className="font-medium">Select Ingredients:</label>
                <Select
                    isMulti
                    options={ingredientOptions}
                    value={selectedIngredients}
                    onChange={setSelectedIngredients}
                    className="mt-2"
                    placeholder="Search or select ingredients..."
                />
            </div>

            <button
                onClick={handleSearch}
                className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600"
            >
                Search Recipes
            </button>

            <div className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
                {recipes.map((recipe) => (
                    <RecipeTile key={recipe.id} recipe={recipe}/>
                ))}
            </div>
        </div>
    );
};

export default WhatsInMyFridgePage;