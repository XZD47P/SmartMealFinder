import {useEffect, useState} from "react";
import {autocompleteIngredients, findRecipesByIngredients} from "../Service/recipeService";
import RecipeTile from "./Recipe/RecipeTile";
import useDebounce from "../Hooks/useDebounce";
import toast from "react-hot-toast";
import Select from "react-select";


const WhatsInMyFridgePage = () => {
    const [selectedIngredients, setSelectedIngredients] = useState([]);
    const [selectedDiet, setSelectedDiet] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [inputValue, setInputValue] = useState(""); //A felhasználó által adott input
    const [ingredientOptions, setIngredientOptions] = useState([]); //Api által javasolt hozzávalók selecthez adása

    const debouncedInput = useDebounce(inputValue, 500);

    const dietOptions = [
        {label: "Gluten Free", value: "gluten free"},
        {label: "Ketogenic", value: "ketogenic"},
        {label: "Vegetarian", value: "vegetarian"},
        {label: "Lacto-Vegetarian", value: "lacto-vegetarian"},
        {label: "Ovo-Vegetarian", value: "ovo-vegetarian"},
        {label: "Vegan", value: "vegan"},
        {label: "Pescetarian", value: "pescetarian"},
        {label: "Paleo", value: "paleo"},
        {label: "Primal", value: "primal"},
        {label: "Low FODMAP", value: "low FODMAP"},
        {label: "Whole30", value: "whole30"},
    ];

    useEffect(() => {
        const fetchIngredients = async () => {
            if (!debouncedInput) {
                setIngredientOptions([]);
                return;
            }

            try {
                const suggestions = await autocompleteIngredients(debouncedInput);
                const options = suggestions.map((item) => ({
                    label: item.name,
                    value: item.name,
                }));
                setIngredientOptions(options);
            } catch (error) {
                toast.error("Failed to fetch ingredients");
                console.error("Error fetching ingredients:", error);
                setIngredientOptions([]);
            }
        };

        fetchIngredients();
    }, [debouncedInput]);

    const handleSearch = async () => {
        const ingridients = selectedIngredients.map((ingredient) => ingredient.value);
        let diets = [];

        if (selectedDiet) {
            diets = selectedDiet.map((diet) => diet.value);
        }

        try {
            const data = await findRecipesByIngredients(ingridients, diets);
            setRecipes(data)
        } catch (error) {
            toast.error("Failed to search for recipes");
        }

    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">What's in Your Fridge?</h1>

            <div className="mb-4">
                <label className="font-medium">Select Ingredients:</label>
                <Select
                    isMulti
                    options={ingredientOptions}
                    onChange={setSelectedIngredients}
                    onInputChange={setInputValue}
                    inputValue={inputValue}
                    placeholder="Type to search ingredients..."
                    className="mt-2"
                />
            </div>
            <div className="mb-4">
                <label className="font-medium">Do you have any dietary restrictions?</label>
                <Select
                    isMulti
                    options={dietOptions}
                    onChange={setSelectedDiet}
                    value={selectedDiet}
                    placeholder="Select a diet (optional)"
                    className="mt-2"
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