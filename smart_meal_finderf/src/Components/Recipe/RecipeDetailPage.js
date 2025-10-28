import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import spoonacular from "../../Backend/spoonacular";
import {motion} from "framer-motion";
import {ListOrdered, Utensils} from "lucide-react";

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
        <motion.div
            className="max-w-4xl mx-auto p-4 md:p-8 bg-white rounded-2xl shadow-lg"
            initial={{opacity: 0, y: 30}}
            animate={{opacity: 1, y: 0}}
            transition={{duration: 0.4}}
        >
            {/* Header */}
            <div className="text-center">
                <h1 className="text-3xl md:text-4xl font-bold mb-3">{recipe.title}</h1>
                <p className="text-gray-500 italic">
                    {recipe.readyInMinutes} minutes • {recipe.servings} servings
                </p>
            </div>

            {/* Image */}
            <div className="flex justify-center mt-6">
                <img
                    src={recipe.image}
                    alt={recipe.title}
                    className="rounded-2xl shadow-md w-full md:w-3/4 object-cover"
                />
            </div>

            {/* Ingredients */}
            <section className="mt-10">
                <div className="flex items-center gap-2 mb-3">
                    <Utensils className="w-5 h-5 text-emerald-600"/>
                    <h2 className="text-2xl font-semibold">Ingredients</h2>
                </div>
                <ul className="bg-emerald-50 p-4 rounded-lg list-disc list-inside space-y-1">
                    {recipe.extendedIngredients.map((ing) => (
                        <li key={ing.id}>{ing.original}</li>
                    ))}
                </ul>
            </section>

            {/* Instructions */}
            {recipe.instructions && (
                <section className="mt-10">
                    <div className="flex items-center gap-2 mb-3">
                        <ListOrdered className="w-5 h-5 text-amber-600"/>
                        <h2 className="text-2xl font-semibold">Instructions</h2>
                    </div>
                    <div
                        className="prose max-w-none prose-emerald"
                        dangerouslySetInnerHTML={{__html: recipe.instructions}}
                    />
                </section>
            )}
        </motion.div>
    );
};


export default RecipeDetailPage;