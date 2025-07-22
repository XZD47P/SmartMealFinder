import {useEffect, useState} from "react";
import toast from "react-hot-toast";
import api from "../../Backend/api";
import {Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";


const ProgressChart = ({refreshTrigger}) => {
    const [data, setData] = useState([]);
    const [metric, setMetric] = useState("weight");

    //Választható filter a charthoz
    const metrics = [
        {key: "weight", label: "Weight (kg)", color: "#4caf50", unit: "kg"},
        {key: "caloriesConsumed", label: "Calories Consumed", color: "#f44336", unit: "kcal"},
        {key: "proteinConsumed", label: "Proteins Consumed", color: "#3f51b5", unit: "g"},
        {key: "carbsConsumed", label: "Carbs Consumed", color: "#ff9800", unit: "g"},
        {key: "fatsConsumed", label: "Fats Consumed", color: "#9c27b0", unit: "g"},
    ];

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.get("/progress/all");
                const sorted = response.data.sort((a, b) => new Date(a.date) - new Date(b.date));
                setData(sorted);

            } catch (error) {
                toast.error("Couldn't load Progress chart!");
                console.log(error);
            }
        };
        fetchData();
    }, [refreshTrigger]);

    const metricInfo = metrics.find(m => m.key === metric);

    return (
        <div className="bg-white rounded-lg shadow-md p-4 mt-6">
            <div className="flex items-center justify-between mb-4">
                <h2 className="text-xl font-semibold">Progress Over Time</h2>
                <select
                    className="border px-3 py-1 rounded-md"
                    value={metric}
                    onChange={(e) => setMetric(e.target.value)}
                >
                    {metrics.map(metric => (
                        <option key={metric.key} value={metric.key}>{metric.label}</option>
                    ))}
                </select>
            </div>

            <ResponsiveContainer width="100%" height={300}>
                <BarChart data={data}>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="date"/>
                    <YAxis unit={metricInfo?.unit || ""}/>
                    <Tooltip/>
                    <Legend/>
                    <Bar
                        dataKey={metric}
                        fill={metricInfo?.color || "#8884d8"}
                        name={metricInfo?.label}
                        barSize={40}
                    />
                </BarChart>
            </ResponsiveContainer>
        </div>
    )

};

export default ProgressChart;