import {Check} from "lucide-react";

const ShoppingListItem = ({item, onToggle}) => {
    return (
        <div
            onClick={() => onToggle(item.id, item.checked)}
            className={`
                group flex items-center gap-4 p-3 mb-2 rounded-xl border transition-all duration-200 cursor-pointer select-none
                ${item.checked
                ? "bg-gray-50 border-gray-100 opacity-60"
                : "bg-white border-gray-200 hover:border-blue-300 shadow-sm hover:shadow-md"
            }
            `}
        >
            <div className={`
                w-6 h-6 min-w-[1.5rem] rounded-full flex items-center justify-center border-2 transition-colors duration-200
                ${item.checked
                ? "bg-green-500 border-green-500"
                : "border-gray-300 group-hover:border-blue-400"
            }
            `}>
                {item.checked && <Check className="w-4 h-4 text-white" strokeWidth={3}/>}
            </div>

            <div
                className={`text-right font-semibold whitespace-nowrap ${item.checked ? "text-gray-400" : "text-blue-600"}`}>
                <span>{item.amount}</span>
                <span className="ml-1 text-sm">{item.unit}</span>
            </div>

            <div className="flex-1">
                <p className={`font-medium capitalize text-gray-800 ${item.checked ? "line-through text-gray-500" : ""}`}>
                    {item.name}
                </p>
            </div>
        </div>
    );
};

export default ShoppingListItem;