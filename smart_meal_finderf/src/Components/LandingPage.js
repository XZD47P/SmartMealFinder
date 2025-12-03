import {useState} from "react";
import {searchRecipes} from "../Service/recipeService";
import toast from "react-hot-toast";
import RecipeTile from "./Recipe/RecipeTile";
import RecommendationSection from "./Recipe/RecommendationSection";
import {useMyContext} from "../Store/ContextApi";

const LandingPage = () => {
    const {currentUser} = useMyContext()
    const [query, setQuery] = useState("");
    const [recipes, setRecipes] = useState([]);

    const handleSearch = async () => {
        try {
            const result = await searchRecipes({query});
            setRecipes(result);
        } catch (error) {
            toast.error("Failed to search recipes.");
        }
    };

    return (
        <div className="p-4 max-w-2xl mx-auto">
            <h1 className="text-4xl font-bold mb-4 text-center">Smart Meal Finder</h1>
            <div className="flex gap-2 mb-4">
                <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    onKeyDown={(e) => {
                        if (e.key === "Enter") {
                            e.preventDefault();
                            handleSearch();
                        }
                    }}
                    className="flex-1 border px-4 py-2 rounded"
                    placeholder="Search for a recipe..."
                />
                <button
                    onClick={handleSearch}
                    className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                >
                    Search
                </button>
            </div>
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 mt-4">
                {recipes ? (
                    recipes.map((recipe) => (
                        <RecipeTile recipe={recipe}/>
                    ))
                ) : (
                    <section className="relative">
                        <div>No recipe found!</div>
                    </section>
                )}
            </div>
            {currentUser && (
                <div>
                    <RecommendationSection
                        profilingEnabled={currentUser?.profilingEnabled}
                    />
                </div>
            )}


        </div>
    );
}

export default LandingPage;