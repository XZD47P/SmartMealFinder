import {useForm} from "react-hook-form";
import InputField from "../Utils/InputField";
import Buttons from "../Utils/Buttons";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {useEffect, useState} from "react";
import {useMyContext} from "../../Store/ContextApi";

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
            caloriesConsumed: "",
            proteinConsumed: "",
            carbsConsumed: "",
            fatsConsumed: "",
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
            caloriesConsumed: dailyProgress.caloriesConsumed,
            proteinConsumed: dailyProgress.proteinConsumed,
            carbsConsumed: dailyProgress.carbsConsumed,
            fatsConsumed: dailyProgress.fatsConsumed,
            comment: dailyProgress.comment,
        })
    }

    const onSubmit = async (data) => {
        const {weight, caloriesConsumed, proteinConsumed, carbsConsumed, fatsConsumed, comment} = data;
        const formData = {
            weight,
            caloriesConsumed,
            proteinConsumed,
            carbsConsumed,
            fatsConsumed,
            comment
        }

        try {
            setLoading(true);
            await api.post("/progress/save", formData, {
                headers: {
                    "Content-Type": "application/json",
                }
            });
            toast.success("Progress saved successfully!");
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
                        label="Calories Consumed"
                        required
                        id="caloriesConsumed"
                        type="number"
                        step="0.01"
                        register={register}
                        errors={errors}
                        message="*Calories are required"
                    />
                    <InputField
                        label="Proteins Consumed (g)"
                        required
                        id="proteinConsumed"
                        type="number"
                        step="0.01"
                        register={register}
                        errors={errors}
                        message="*Proteins are required"
                    />
                    <InputField
                        label="Carbs Consumed (g)"
                        required
                        id="carbsConsumed"
                        type="number"
                        step="0.01"
                        register={register}
                        errors={errors}
                        message="*Carbs are required"
                    />
                    <InputField
                        label="Fats Consumed (g)"
                        required
                        id="fatsConsumed"
                        type="number"
                        step="0.01"
                        register={register}
                        errors={errors}
                        message="*Fats are required"
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
        </div>
    );
};

export default DailyProgressForm;