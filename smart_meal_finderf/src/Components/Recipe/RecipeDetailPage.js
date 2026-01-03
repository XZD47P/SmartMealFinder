import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {motion} from "framer-motion";
import {ListOrdered, Utensils} from "lucide-react";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import {IconButton} from "@mui/material";
import FavoriteIcon from "@mui/icons-material/Favorite";
import {useMyContext} from "../../Store/ContextApi";
import StarIcon from "@mui/icons-material/Star";
import Buttons from "../Utils/Buttons";
import useEngagementTracker from "../../Hooks/useEngagementTracker";

const RecipeDetailPage = () => {
    const {id} = useParams();
    const [recipe, setRecipe] = useState(null);
    const [isLiked, setIsLiked] = useState(false);
    const [likeCount, setLikeCount] = useState(0);
    const {currentUser} = useMyContext();
    const [isFavourite, setIsFavourite] = useState(false);

    useEngagementTracker(recipe, currentUser);

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const response = await api.get("food-api/recipe",
                    {params: {id: id}});
                setRecipe(response.data);

                const count = await api.get("/recipe/likeCount", {params: {id}});
                setLikeCount(count.data);
            } catch (error) {
                console.error("Failed to fetch recipe details", error);
            }
        };
        fetchDetails();
    }, [id]);

    useEffect(() => {
        if (!currentUser || !recipe) return;

        const fetchUserState = async () => {
            try {
                const liked = await api.get("/recipe/isLiked", {params: {id}});
                setIsLiked(liked.data);

                const favourite = await api.get("/recipe/isFavourite", {params: {id}});
                setIsFavourite(favourite.data);
            } catch (error) {
                console.error("Failed to fetch user recipe state", error);
            }
        };

        fetchUserState();
    }, [currentUser, recipe, id]);

    const handleLike = async () => {
        try {
            if (isLiked) {
                await api.delete("/recipe/like", {data: recipe});
                setLikeCount(prev => prev - 1);
                toast.success("Recipe like removed!");
            } else {
                await api.post("/recipe/like", recipe);
                setLikeCount(prev => prev + 1);
                toast.success("Recipe liked!");
            }

            setIsLiked(prev => !prev);
        } catch (error) {
            toast.error("Liking recipe failed");
            console.error("Failed to like recipe", error);
        }
    };

    const handleFavourite = async () => {
        try {
            if (isFavourite) {
                await api.delete("/recipe/favourite", {data: recipe});
                toast.success("Removed from favourites");
            } else {
                await api.post("/recipe/favourite", recipe);
                toast.success("Added to favourites");
            }

            setIsFavourite(prev => !prev);

        } catch (error) {
            toast.error("Failed to update favourites");
            console.error("Failed to update favourites", error);
        }
    };

    return (
        <>
            {!recipe ? (
                <div className="flex justify-center items-center h-screen">
                    <div>Loading...</div>
                </div>
            ) : (
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
                        <div className="flex justify-center items-center mt-4 gap-8">

                            <div className="flex items-center gap-2">
                                <motion.div
                                    animate={{scale: isLiked ? 1.3 : 1}}
                                    transition={{duration: 0.2}}
                                >
                                    <IconButton onClick={handleLike} disabled={!currentUser}>
                                        <FavoriteIcon
                                            style={{
                                                color: isLiked ? "red" : "grey",
                                                fontSize: "2rem",
                                                transition: "color 0.2s"
                                            }}
                                        />
                                    </IconButton>
                                </motion.div>
                                <span className="text-lg font-semibold">{likeCount}</span>
                            </div>

                            <motion.div
                                animate={{scale: isFavourite && currentUser ? 1.3 : 1}}
                                transition={{duration: 0.2}}
                            >
                                <Buttons
                                    disabled={!currentUser}
                                    onClickhandler={handleFavourite}
                                    className={`flex items-center gap-2 px-4 py-2 rounded-lg border transition 
                                ${isFavourite
                                        ? "border-yellow-500 text-yellow-600"
                                        : "border-gray-400 text-gray-600"}
                                `}
                                >
                                    <StarIcon
                                        style={{
                                            color: isFavourite ? "#f6c000" : "grey",
                                            width: "20px",
                                            height: "20px"
                                        }}
                                    />
                                    {isFavourite ? "Favourite" : "Add to favourites"}
                                </Buttons>
                            </motion.div>
                        </div>
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
            )}
        </>
    );
};


export default RecipeDetailPage;