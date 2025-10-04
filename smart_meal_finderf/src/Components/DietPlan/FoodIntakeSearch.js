import {useState} from "react";
import spoonacular from "../../Backend/spoonacular";
import toast from "react-hot-toast";
import Buttons from "../Utils/Buttons";
import {saveFoodEntry} from "../../Service/recipeService";

const FoodIntakeSearch = ({onSuccess}) => {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);

    const searchProducts = async () => {
        setLoading(true);
        try {
            const [productRes, ingredientRes] = await Promise.all([
                spoonacular.get("/food/products/search", {
                    params: {query, number: 5}
                }),
                spoonacular.get("/food/ingredients/search", {
                    params: {query, number: 5}
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
            setQuery("")
            setResults([]);
            onSuccess();
            toast.success("Product Added");
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
                                     onClickhandler={() => handleAdd(item)}
                                     className={"bg-green-500 text-white px-2 py-1 rounded"}
                            >
                                Add
                            </Buttons>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    )
}

export default FoodIntakeSearch;