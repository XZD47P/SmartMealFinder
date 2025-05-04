import {Link} from "react-router-dom";

const RecipeTile = ({recipe}) => (
    <Link to={`/recipes/${recipe.id}`}>
        <div className="border p-4 rounded shadow hover:shadow-lg transition">
            <img src={recipe.image} alt={recipe.title} className="w-full h-40 object-cover rounded"/>
            <h3 className="text-lg font-semibold mt-2">{recipe.title}</h3>
        </div>
    </Link>
);

export default RecipeTile;