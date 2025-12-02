import {useEffect, useState} from "react";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import RecipeTile from "./RecipeTile";
import {useMyContext} from "../../Store/ContextApi";

const FavouriteRecipesPage = () => {
    const [recipes, setRecipes] = useState(null);
    const {currentUser} = useMyContext();

    useEffect(() => {
        const loadFavouriteRecipes = async () => {
            try {
                const favRecipes = await api.get("/recipe/favourite");
                setRecipes(favRecipes.data);
            } catch (error) {
                toast.error("Failed to fetch favorite recipes.");
            }
        };
        loadFavouriteRecipes();
    }, [currentUser]);

    return (
        <div className="p-4 max-w-2xl mx-auto">
            <h1 className="text-4xl font-bold mb-4 text-center">Your favourite recipes</h1>
            <div className="text-center">
                Here you can see your favourite recipes.<br/>
                If you dont have any, open a recipe you like, and press the Favourite button!
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 mt-4">
                {recipes ? (
                    recipes.map((recipe) => (
                        <RecipeTile key={recipe.id} recipe={recipe}/>
                    ))
                ) : (
                    <section className="relative">
                        <div>No favourite recipe found!</div>
                    </section>
                )}
            </div>
        </div>
    );
}

export default FavouriteRecipesPage;