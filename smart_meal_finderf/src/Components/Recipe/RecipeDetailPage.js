import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import spoonacular from "../../Backend/spoonacular";

const RecipeDetailPage = () => {
    const {id} = useParams();
    const [recipe, setRecipe] = useState(null);

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const response = await spoonacular.get(`/recipes/${id}/information`);
                setRecipe(response.data);
            } catch (error) {
                console.error("Failed to fetch recipe details", error);
            }
        };
        fetchDetails();
    }, [id]);

    if (!recipe) return <div>Loading...</div>;

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-2">{recipe.title}</h1>
            <img src={recipe.image} alt={recipe.title} className="w-full max-w-md rounded shadow mb-4"/>
            <h2 className="text-xl font-semibold mt-4">Ingredients</h2>
            <ul className="list-disc pl-5">
                {recipe.extendedIngredients.map((ing) => (
                    <li key={ing.id}>{ing.original}</li>
                ))}
            </ul>

            <h2 className="text-xl font-semibold mt-4">Instructions</h2>
            <p dangerouslySetInnerHTML={{__html: recipe.instructions}}/>
        </div>
    );
};

export default RecipeDetailPage;