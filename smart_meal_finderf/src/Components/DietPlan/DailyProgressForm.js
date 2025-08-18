import {useForm} from "react-hook-form";
import InputField from "../Utils/InputField";
import Buttons from "../Utils/Buttons";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {useEffect, useState} from "react";
import {useMyContext} from "../../Store/ContextApi";
import ProductSearch from "./ProductSearch";

const DailyProgressForm = ({onSuccess}) => {

    const [loading, setLoading] = useState(false);
    const {currentUser} = useMyContext();
    const [dailyProgress, setDailyProgress] = useState(null);

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
        fetchDailyProgress();
    }, [currentUser]);

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
            onSuccess();
        } catch (error) {
            console.error(error);
            toast.error("Failed to save progress.");
        } finally {
            setLoading(false);
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
                <ProductSearch onSuccess={fetchDailyProgress}/>
            </div>
        </div>
    );
};

export default DailyProgressForm;