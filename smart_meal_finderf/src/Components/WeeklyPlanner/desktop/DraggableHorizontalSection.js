import {useRef} from "react";
import {FaChevronLeft, FaChevronRight} from "react-icons/fa";
import DraggableRecipeTile from "./DraggableRecipeTile";

const DraggableHorizontalSection = ({title, recipes}) => {
    const scrollRef = useRef(null);

    const scroll = (direction) => {
        if (!scrollRef.current) return;
        const {scrollLeft, clientWidth} = scrollRef.current;
        const scrollAmount = clientWidth * 0.8;

        scrollRef.current.scrollTo({
            left: direction === "left" ? scrollLeft - scrollAmount : scrollLeft + scrollAmount,
            behavior: "smooth",
        });
    };

    if (!recipes || recipes.length === 0) {
        return (
            <section className="relative">
                <h2 className="text-xl font-bold mb-4">{title}</h2>
                <div>No recipe found!</div>
            </section>
        );
    }

    return (
        recipes ? (
            <section className="relative">
                <h2 className="text-xl font-bold mb-4">{title}</h2>

                <button
                    className="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 bg-white p-2 rounded-full shadow hover:bg-gray-100"
                    onClick={() => scroll("left")}
                >
                    <FaChevronLeft/>
                </button>

                <button
                    className="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 bg-white p-2 rounded-full shadow hover:bg-gray-100"
                    onClick={() => scroll("right")}
                >
                    <FaChevronRight/>
                </button>

                <div
                    ref={scrollRef}
                    className="flex gap-4 overflow-x-auto scrollbar-hide scroll-smooth"
                >
                    {recipes.map((recipe) => (
                        <div key={`${title}-${recipe.id}`} className="flex-shrink-0 w-64">
                            <DraggableRecipeTile recipe={recipe}/>
                        </div>
                    ))}
                </div>
            </section>
        ) : (
            <section className="relative">
                <h2 className="text-xl font-bold mb-4">{title}</h2>
                <div>No recipe found!</div>
            </section>
        )
    );
};

export default DraggableHorizontalSection;
