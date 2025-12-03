import {useEffect, useState} from "react";
import HorizontalSection from "../Utils/HorizontalSection";
import api from "../../Backend/api";
import {useMyContext} from "../../Store/ContextApi";
import toast from "react-hot-toast";

const RecommendationSection = ({profilingEnabled}) => {
    const {fetchUser} = useMyContext()
    const [recipes, setRecipes] = useState(null);
    const [loading, setLoading] = useState(false);

    const enableProfiling = async () => {
        try {
            setLoading(true);
            await api.put("/auth/user/enable-profiling");
            fetchUser();
            setLoading(false);
            toast.success("Profiling enabled");
        } catch (error) {
            toast.error("There was an error while enabling profiling");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!profilingEnabled) return;

        const loadRecommended = async () => {
            setLoading(true);
            try {
                const res = await api.get("/recipe/recommendations");
                setRecipes(res.data);
            } catch (e) {
                console.error("Failed to load recommendations");
            } finally {
                setLoading(false);
            }
        };

        loadRecommended();
    }, [profilingEnabled]);

    if (!profilingEnabled) {
        // ⛔ PROFILING DISABLED → Show blurred overlay
        return (
            <div className="relative p-4 rounded-xl bg-gray-100 mt-8">
                <div className="blur-sm opacity-40 pointer-events-none select-none">
                    <HorizontalSection title="Recommended for You" recipes={[]}/>
                </div>

                <div className="absolute inset-0 flex flex-col items-center justify-center">
                    <p className="font-semibold mb-3 text-center">
                        Enable profiling to see personalized recommendations.
                    </p>
                    <button
                        onClick={enableProfiling}
                        className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
                    >
                        Enable Profiling
                    </button>
                </div>
            </div>
        );
    }
    if (loading) return <p className="mt-4">Loading recommendations...</p>;

    return (
        <HorizontalSection
            title="Recommended for You"
            recipes={recipes || []}
        />
    );
};

export default RecommendationSection;