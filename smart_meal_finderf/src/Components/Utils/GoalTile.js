const GoalTile = ({title, value}) => {
    return (
        <div className="bg-white p-4 rounded-lg shadow hover:shadow-md transition">
            <h3 className="font-bold text-lg text-green-600">{title}</h3>
            <p>{value}</p>
        </div>
    );
};

export default GoalTile;