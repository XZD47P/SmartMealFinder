const SelectField = ({
                         id,
                         label,
                         required,
                         register,
                         errors,
                         options,
                         message,
                     }) => {
    return (
        <div className="flex flex-col">
            <label htmlFor={id} className="mb-1 font-semibold">
                {label}
                {required && <span className="text-red-500"> *</span>}
            </label>

            <select
                id={id}
                {...register(id, {required: message})}
                className="border rounded-md p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                defaultValue=""
            >
                <option value="" disabled>Select {label}</option>
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>

            {errors[id] && (
                <span className="text-red-500 text-sm mt-1">{errors[id].message}</span>
            )}
        </div>
    );
};

export default SelectField;