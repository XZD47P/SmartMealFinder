import {useState} from "react";
import toast from "react-hot-toast";
import Buttons from "../Utils/Buttons";
import {saveFoodEntry} from "../../Service/recipeService";
import FoodEntryQuantityModal from "../Utils/FoodEntryQuantityModal";
import {useMyContext} from "../../Store/ContextApi";
import api from "../../Backend/api";

const FoodEntrySearch = () => {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedItem, setSelectedItem] = useState(null);
    const [open, setOpen] = useState(false);
    const {triggerProgressRefresh} = useMyContext();

    const searchProducts = async () => {
        setLoading(true);
        try {
            const [productRes, ingredientRes] = await Promise.all([
                api.get("food-api/products/search", {
                    params: {query}
                }),
                api.get("food-api/food/ingredients/search", {
                    params: {query}
                })
            ]);

            const products = (productRes.data.products || []).map(p => ({
                id: p.id,
                name: p.title,
                type: "product"
            }));

            const ingredients = (ingredientRes.data.results || []).map(i => ({
                id: i.id,
                name: i.name,
                type: "ingredient"
            }));

            setResults([...products, ...ingredients]);
        } catch (error) {
            console.log(error);
            toast.error("Error while searching products");
        } finally {
            setLoading(false);
        }
    }

    const handleAdd = async (item) => {
        try {
            await saveFoodEntry(item);
            setOpen(false);
            setSelectedItem(null);
            setQuery("")
            setResults([]);
            triggerProgressRefresh();
            toast.success("Successfully added " + item.name + " to your daily intake");
        } catch (error) {
            console.log(error);
            toast.error("Error while adding product");
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            searchProducts();
        }
    }

    return (
        <div className="mb-4">
            <div className="flex gap-2">
                <input
                    type="text"
                    value={query}
                    onChange={e => setQuery(e.target.value)}
                    onKeyDown={handleKeyDown}
                    placeholder="Search for a product..."
                    className="flex-1 border px-3 py-2 rounded"/>
                <Buttons disabled={loading} type="button" onClickhandler={searchProducts}
                         className={"bg-blue-500 text-white px-4 py-2 rounded"}>
                    {loading ? "Searching..." : "Search"}
                </Buttons>
            </div>

            {results.length > 0 && (
                <ul className="mt-2 border rounded divide-y">
                    {results.map(item => (
                        <li key={item.id} className="flex justify-between items-center p-2">
                            <span>{item.name}</span>
                            <Buttons type={"button"}
                                     onClickhandler={() => {
                                         setSelectedItem(item);
                                         setOpen(true)
                                     }}
                                     className={"bg-green-500 text-white px-2 py-1 rounded"}
                            >
                                Add
                            </Buttons>
                        </li>
                    ))}
                </ul>
            )}
            <FoodEntryQuantityModal
                open={open}
                onClose={() => {
                    setSelectedItem(null);
                    setOpen(false)
                }
                }
                itemName={selectedItem?.name}
                itemType={selectedItem?.type}
                onConfirm={({quantity, unit}) => handleAdd({...selectedItem, quantity, unit})}
            />
        </div>
    )
}

export default FoodEntrySearch;