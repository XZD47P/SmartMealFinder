import {useState} from "react";
import RecipeTileMobile from "./RecipeTileMobile";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const MobileSection = ({title, recipes = [], onAdd, defaultOpen = true}) => {
    const [isOpen, setIsOpen] = useState(defaultOpen);

    return (
        <div className="mb-4 bg-white rounded-xl border border-gray-200 overflow-hidden shadow-sm">
            {/*Toggle button*/}
            <button
                onClick={() => setIsOpen(!isOpen)}
                className="w-full flex justify-between items-center p-4 bg-gray-50 hover:bg-gray-100 transition-colors"
            >
                <h3 className="text-lg font-bold text-gray-700">
                    {title} <span className="text-sm font-normal text-gray-400 ml-1">({recipes.length})</span>
                </h3>
                <ExpandMoreIcon
                    className={`text-gray-500 transform transition-transform duration-200 ${isOpen ? "rotate-180" : ""}`}
                />
            </button>

            {isOpen && (
                <div className="p-3 bg-white flex flex-col gap-3 border-t border-gray-100">
                    {recipes && recipes.length > 0 ? (
                        recipes.map((recipe) => (
                            <RecipeTileMobile
                                key={recipe.id}
                                recipe={recipe}
                                onAdd={onAdd}
                            />
                        ))
                    ) : (
                        <div className="text-center py-4 text-gray-400 text-sm">
                            No recipes found!
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default MobileSection;