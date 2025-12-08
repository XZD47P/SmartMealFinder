import MobileSection from "./MobileSection";

const RecommendationListMobile = ({onRecipeSelect, recipes}) => {


    return (
        <div className="pb-10 space-y-2">
            {/* Optional Search Bar here */}

            <MobileSection
                title={"Recommended based on your activity"}
                recipes={recipes.personal}
                onAdd={onRecipeSelect}
                defaultOpen={false}
            />

            <MobileSection
                title={"🍲 Soup recommendations"}
                recipes={recipes.soup}
                onAdd={onRecipeSelect}
                defaultOpen={false}
            />
            <MobileSection
                title={"🍽️ Main Course recommendations"}
                recipes={recipes.main_course}
                onAdd={onRecipeSelect}
                defaultOpen={false}
            />
            <MobileSection
                title={"🍰 Snack recommendations"}
                recipes={recipes.snack}
                onAdd={onRecipeSelect}
                defaultOpen={false}
            />
        </div>
    );
};

export default RecommendationListMobile;