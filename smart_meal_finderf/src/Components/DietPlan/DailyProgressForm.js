import {useForm} from "react-hook-form";
import InputField from "../Utils/InputField";
import Buttons from "../Utils/Buttons";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {useEffect, useState} from "react";
import {useMyContext} from "../../Store/ContextApi";
import FoodEntrySearch from "./FoodEntrySearch";

const DailyProgressForm = () => {

    const [loading, setLoading] = useState(false);
    const {currentUser, refreshProgress, triggerProgressRefresh} = useMyContext();
    const [dailyProgress, setDailyProgress] = useState(null);
    const [foodEntries, setFoodEntries] = useState([]);

    const {
        register,
        handleSubmit,
        reset,
        formState: {errors},
    } = useForm({
        defaultValues: {
            weight: "",
            comment: "",
        },
        mode: "onTouched",
    });

    const fetchDailyProgress = async () => {
        try {
            setLoading(true);
            const response = await api.get("/progress");
            if (response.data) {
                setDailyProgress(response.data);
            } else {
                setDailyProgress(null);
            }
        } catch (error) {
            toast.error("Daily progress could not be loaded!");
            setLoading(false);
        } finally {
            setLoading(false);
        }

    }


    useEffect(() => {
        if (!currentUser) return;
        fetchDailyProgress();
        fetchFoodEntries();
    }, [currentUser]);

    useEffect(() => {
        if (!currentUser) return;
        fetchDailyProgress();
        fetchFoodEntries();
    }, [refreshProgress, currentUser]);

    useEffect(() => {
        if (dailyProgress) {
            loadDailyProgress();
        }
    }, [dailyProgress, reset]);

    const loadDailyProgress = () => {
        reset({
            weight: dailyProgress.weight,
            comment: dailyProgress.comment,
        })
    }

    const onSubmit = async (data) => {
        const {weight, comment} = data;
        const formData = {
            weight,
            comment
        }

        try {
            setLoading(true);
            await api.post("/progress/weight/save", formData, {
                headers: {
                    "Content-Type": "application/json",
                }
            });
            toast.success("Progress saved successfully!");
            await fetchDailyProgress();
            triggerProgressRefresh();
        } catch (error) {
            console.error(error);
            toast.error("Failed to save progress.");
        } finally {
            setLoading(false);
        }
    };

    const fetchFoodEntries = async () => {
        try {
            const response = await api.get("/food-entry/list");
            if (response.data) {
                setFoodEntries(response.data);
            } else {
                setFoodEntries([]);
            }
        } catch (error) {
            console.error(error);
            toast.error("Could not load food entries");
        }
    };

    const handleDeleteFoodEntry = async (foodEntryId) => {
        try {
            await api.delete(`/food-entry/delete/${foodEntryId}`);
            toast.success("Food entry removed!");
            await fetchDailyProgress();
            triggerProgressRefresh();
        } catch (error) {
            console.error(error);
            toast.error("Failed to delete food entry");
        }
    };


    return (
        <div className="bg-yellow-100 p-4 rounded-md shadow-md mt-6">
            <h2 className="text-xl font-semibold mb-2">Daily Progress</h2>
            <form onSubmit={handleSubmit(onSubmit)}>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <InputField
                        label="Weight (kg)"
                        required
                        id="weight"
                        type="number"
                        step="0.01"
                        register={register}
                        errors={errors}
                        message="*Weight is required"
                    />
                    <InputField
                        label="Comment"
                        id="comment"
                        type="text"
                        register={register}
                        errors={errors}
                        placeholder="Optional comment"
                    />
                </div>
                <Buttons
                    disabled={loading}
                    type="submit"
                    className="bg-green-600 text-white w-full mt-4 py-2 rounded hover:bg-green-700 transition-colors"
                >
                    {loading ? "Submitting..." : "Submit Progress"}
                </Buttons>
            </form>
            {dailyProgress && ( //Azért szükséges ez a forma, mert máskülönben nem tudja a nullokat renderelni a program
                <>
                    <div>
                        <label className="block font-medium">Calories Consumed</label>
                        <p>{dailyProgress.caloriesConsumed} kcal</p>
                    </div>
                    <div>
                        <label className="block font-medium">Proteins Consumed</label>
                        <p>{dailyProgress.proteinConsumed} g</p>
                    </div>
                    <div>
                        <label className="block font-medium">Carbs Consumed</label>
                        <p>{dailyProgress.carbsConsumed} g</p>
                    </div>
                    <div>
                        <label className="block font-medium">Fats Consumed</label>
                        <p>{dailyProgress.fatsConsumed} g</p>
                    </div>
                </>
            )}
            <div>
                <FoodEntrySearch/>
            </div>
            <div>
                <ul className="divide-y border rounded bg-white">
                    {foodEntries.map(entry => (
                        <li key={entry.id} className="flex justify-between items-center p-2">
                            <div>
                                <p className="font-medium">{entry.quantity} {entry.unit} of {entry.name}</p>
                                <p className="text-sm text-gray-600">
                                    {entry.calories} kcal | {entry.protein} g protein | {entry.carbs} g carbs
                                    | {entry.fats} g fat
                                </p>
                            </div>
                            <div className="flex items-center gap-2">
                                <span className="text-xs text-gray-500">{entry.date}</span>
                                <button
                                    onClick={() => handleDeleteFoodEntry(entry.id)}
                                    className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600"
                                >
                                    ✕
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default DailyProgressForm;