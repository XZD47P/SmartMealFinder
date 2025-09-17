import {useMyContext} from "../../Store/ContextApi";
import {useEffect, useState} from "react";
import api from "../../Backend/api";
import {useForm} from "react-hook-form";
import InputField from "../Utils/InputField";
import Buttons from "../Utils/Buttons";
import toast from "react-hot-toast";
import SelectField from "../Utils/SelectField";
import {ClockLoader} from "react-spinners";
import GoalTile from "../Utils/GoalTile";
import DailyProgressForm from "./DailyProgressForm";
import ProgressChart from "./ProgressChart";

const Plan = () => {
    const {currentUser, token} = useMyContext();
    const [dietPlan, setDietPlan] = useState(null);
    const [loading, setLoading] = useState(false);
    const [refreshChart, setRefreshChart] = useState(false);
    const [goalOptions, setGoalOptions] = useState([]);


    const fetchDietPlan = async () => {
        try {
            const response = await api.get("/plan");
            setDietPlan(response.data);
        } catch (error) {
            setDietPlan(null);
        } finally {
            setLoading(false);
        }
    };

    const deleteDietPlan = async () => {
        try {
            setLoading(true);
            if (window.confirm("Are you sure you want to delete this plan?")) {
                await api.delete("/plan");
                toast.success("Diet plan deleted successfully!");
                window.location.reload();
            }
        } catch (error) {
            toast.error("Failed to delete diet plan, please try again later!");
        } finally {
            setLoading(false);
        }
    }

    const fetchGoals = async () => {
        try {
            const response = await api.get("/fitness-goal/all");
            const mappedGoals = response.data.map(data => ({
                value: data.id,
                label: `${data.goal} ${data.deltaWeight}kg a week`,
            }));
            setGoalOptions(mappedGoals);
        } catch (error) {
            console.log(error);
            toast.error("Failed to load goals");
        }
    }

    useEffect(() => {

        fetchDietPlan();
        fetchGoals();
    }, [currentUser]);

    //Form létrehozása
    const {
        register,
        handleSubmit,
        reset,
        setError,
        formState: {errors},
    } = useForm({
        defaultValues: {
            sex: "",
            weight: "",
            height: "",
            age: "",
            activityLevel: "",
            goalType: "",
            weightGoal: "",
            daysToReachGoal: "",
        },
        mode: "onTouched",
    });

    const onSubmitHandler = async (data) => {
        const {sex, weight, height, age, activityLevel, goalType, weightGoal} = data;
        const formData = {
            sex,
            weight,
            height,
            age,
            activityLevel,
            goalType,
            weightGoal
        }

        try {
            setLoading(true);
            await api.post("/plan/create", formData, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
            toast.success("Diet plan successfully created!");
            reset();
            await fetchDietPlan();
        } catch (error) {
            toast.error("Something went wrong! Please try again!");
            console.log(error.message);
        } finally {
            setLoading(false);
        }
    }

    const triggerChartRefresh = () => {
        setRefreshChart(refresh => !refresh);
    };

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold mb-4">My Diet Plan</h1>
            {loading ? (
                <div className="flex justify-center items-center min-h-[200px]">
                    <ClockLoader
                        color="green"
                        loading={loading}
                        size={50}
                        aria-label="Loading Spinner"
                        data-testid="loader"
                    />
                </div>
            ) : (
                <>
                    {dietPlan ? (
                        <div className="bg-green-100 p-4 rounded-md shadow-md">
                            <h2 className="text-xl font-semibold mb-2">Your Current Goals:</h2>
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                                <GoalTile title="Goal Weight" value={`${dietPlan.goalWeight} kg`}/>
                                <GoalTile title="Goal Date" value={dietPlan.goalDate}/>
                                <GoalTile title="Goal Calories" value={`${dietPlan.goalCalorie} kcal/day`}/>
                                <GoalTile title="Goal Proteins" value={`${dietPlan.goalProtein} g/day`}/>
                                <GoalTile title="Goal Carbs" value={`${dietPlan.goalCarbohydrate} g/day`}/>
                                <GoalTile title="Goal Fats" value={`${dietPlan.goalFat} g/day`}/>
                                <Buttons disabled={loading}
                                         className="bg-red-500 hover:bg-red-600 text-white py-2 px-4 rounded"
                                         onClickhandler={deleteDietPlan}>{loading ? "Loading..." : "Delete plan"}</Buttons>
                            </div>
                            <DailyProgressForm onSuccess={triggerChartRefresh}/>
                            <ProgressChart refreshTrigger={refreshChart}/>
                        </div>

                    ) : (
                        <div className="bg-blue-100 p-4 rounded-md shadow-md w-1/2 m-auto">
                            <h2 className="text-xl font-semibold mb-2">You don't have a diet plan yet</h2>
                            <p>Create a new diet plan below:</p>
                            <form onSubmit={handleSubmit(onSubmitHandler)}>
                                <div className="flex flex-col gap-2">
                                    <SelectField
                                        label="Sex"
                                        required
                                        id="sex"
                                        register={register}
                                        errors={errors}
                                        options={[
                                            {value: "male", label: "Male"},
                                            {value: "female", label: "Female"}
                                        ]}
                                    />
                                    <InputField
                                        label="Weight"
                                        required
                                        id="weight"
                                        type="number"
                                        message="*Weight is required"
                                        placeholder="type your weight"
                                        register={register}
                                        errors={errors}
                                    />
                                    <InputField
                                        label="Height"
                                        required
                                        id="height"
                                        type="number"
                                        message="*Height is required"
                                        placeholder="type your height"
                                        register={register}
                                        errors={errors}
                                    />
                                    <InputField
                                        label="Age"
                                        required
                                        id="age"
                                        type="number"
                                        message="*Age is required"
                                        placeholder="type your age"
                                        register={register}
                                        errors={errors}
                                    />
                                    <SelectField
                                        label="Activity Level"
                                        required
                                        id="activityLevel"
                                        register={register}
                                        errors={errors}
                                        options={[
                                            {value: 1, label: "Little to No Exercise"},
                                            {value: 2, label: "Exercise 1-3 days per week"},
                                            {value: 3, label: "Exercise 3-5 days per week"},
                                            {value: 4, label: "Exercise 6-7 days per week"},
                                            {value: 5, label: "Exercises hardly or doing physical job"},
                                        ]}
                                    />
                                    <SelectField
                                        label="Goal"
                                        required
                                        id="goalType"
                                        register={register}
                                        errors={errors}
                                        options={goalOptions}
                                    />
                                    <InputField
                                        label="Goal weight"
                                        required
                                        id="weightGoal"
                                        type="number"
                                        message="*Goal weight is required"
                                        placeholder="type your goal weight"
                                        register={register}
                                        errors={errors}
                                    />
                                </div>
                                <Buttons
                                    disabled={loading}
                                    className="bg-blue-500 font-semibold flex justify-center text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-sm my-3"
                                    type="submit"
                                >
                                    {loading ? <span>Loading...</span> : "Create plan"}
                                </Buttons>
                            </form>

                        </div>
                    )}
                </>
            )}
        </div>
    )
}

export default Plan;