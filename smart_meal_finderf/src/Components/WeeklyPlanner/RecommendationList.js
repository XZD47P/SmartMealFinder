import DraggableRecipeTile from "./DraggableRecipeTile";

const mockRecipes = [
    {id: "1", title: "Pizza"},
    {id: "2", title: "Pasta"},
    {id: "3", title: "Chicken"},
    {id: "4", title: "Beef"},
];

const RecommendationList = () => {
    return (
        <div className="space-y-4">
            <h2 className="text-xl font-bold">Recommended Recipes</h2>
            <div className="grid gap-3">
                {mockRecipes.map(recipe => (
                    <DraggableRecipeTile key={recipe.id} recipe={recipe}/>
                ))}
            </div>
        </div>
    );
};

export default RecommendationList;
