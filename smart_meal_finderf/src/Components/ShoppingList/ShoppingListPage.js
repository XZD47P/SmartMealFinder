import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {ArrowLeft} from "lucide-react";
import api from "../../Backend/api";
import {getISOWeekInfo} from "../Utils/DateUtils";
import ShoppingListItem from "./ShoppingListItem";
import toast from "react-hot-toast";
import {CircularProgress} from "@mui/material";

const ShoppingListPage = () => {
    const navigate = useNavigate();
    const location = useLocation(); // Hook, hogy elérjük a kapott listát

    const passedList = location.state?.list;
    const [items, setItems] = useState(passedList || []);
    const [loading, setLoading] = useState(!passedList);
    const {year, weekNumber} = getISOWeekInfo(new Date());

    useEffect(() => {
        // Ha van listánk, ne kérjünk le másikat
        if (passedList) {
            return;
        }

        const fetchList = async () => {
            try {
                setLoading(true);
                const response = await api.get("/shopping-list/load", {
                    params: {year, week: weekNumber}
                });
                if (response.data) {
                    setItems(response.data);
                }
            } catch (error) {
                console.error("Failed to load list", error);
                toast.error("Could not load shopping list");
            } finally {
                setLoading(false);
            }
        };

        fetchList();
    }, [year, weekNumber, passedList]);

    const handleToggle = async (itemId, currentStatus) => {
        // Először lekezeljük a váltást, ha sikertelen akkor visszaállítjuk
        const newStatus = !currentStatus;
        setItems(prevItems =>
            prevItems.map(item =>
                item.id === itemId ? {...item, checked: newStatus} : item
            )
        );

        try {
            await api.patch(`/shopping-list/item/${itemId}/toggle`, null, {
                params: {checked: newStatus}
            });
        } catch (error) {
            toast.error("Failed to update item");
            setItems(prevItems =>
                prevItems.map(item =>
                    item.id === itemId ? {...item, checked: currentStatus} : item
                )
            );
        }
    };

    const activeItems = items.filter(i => !i.checked);
    const completedItems = items.filter(i => i.checked);

    if (loading) {
        return (
            <div className="flex h-screen items-center justify-center">
                <CircularProgress/>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 pb-20">
            <div className="sticky top-0 z-10 bg-white shadow-sm px-4 py-4 md:px-8">
                <div className="max-w-2xl mx-auto flex items-center justify-between">
                    <button
                        onClick={() => navigate(-1)}
                        className="p-2 hover:bg-gray-100 rounded-full transition-colors"
                    >
                        <ArrowLeft className="text-gray-600"/>
                    </button>
                    <h1 className="text-xl font-bold text-gray-800">Shopping List</h1>
                    <div className="w-10"/>
                </div>
            </div>

            <div className="max-w-2xl mx-auto px-4 mt-6">

                {items.length === 0 && (
                    <div className="text-center py-20 text-gray-400">
                        <p className="text-lg">Your list is empty.</p>
                        <p className="text-sm">Plan some meals to get started!</p>
                    </div>
                )}

                {/* Még meg kell venni */}
                <div className="space-y-1">
                    {activeItems.map(item => (
                        <ShoppingListItem key={item.id} item={item} onToggle={handleToggle}/>
                    ))}
                </div>

                {/* Megvett dolgok */}
                {completedItems.length > 0 && (
                    <div className="mt-8">
                        <h3 className="text-sm font-bold text-gray-400 uppercase tracking-wider mb-3 px-2">
                            Completed
                        </h3>
                        <div className="space-y-1">
                            {completedItems.map(item => (
                                <ShoppingListItem key={item.id} item={item} onToggle={handleToggle}/>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ShoppingListPage;