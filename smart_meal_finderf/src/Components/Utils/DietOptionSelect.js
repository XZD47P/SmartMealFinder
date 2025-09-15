import {useEffect, useState} from "react";
import Select from "react-select";
import api from "../../Backend/api";
import toast from "react-hot-toast";

const DietOptionSelect = ({value, onChange, placeholder}) => {
    const [dietOptions, setDietOptions] = useState([]);

    useEffect(() => {
        const fetchDietOptions = async () => {
            try {
                const response = await api.get("/diet-option/list");
                const mappedDietOptions = response.data.map(item => ({
                    label: item.label,
                    value: item.apiValue,
                }));
                setDietOptions(mappedDietOptions);
            } catch (error) {
                toast.error("There was an error while retrieving diet options");
            }
        };
        fetchDietOptions();
    }, []);

    return (
        <div className="mb-4">
            <label className="font-medium">Do you have any dietary restrictions?</label>
            <Select
                isMulti
                options={dietOptions}
                onChange={onChange}
                value={value}
                placeholder={placeholder}
                className="mt-2"
            />
        </div>
    );
};

export default DietOptionSelect;