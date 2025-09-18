import {useEffect, useState} from "react";
import Select from "react-select";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import {useMyContext} from "../../Store/ContextApi";

const DietOptionSelect = ({value, onChange, placeholder}) => {
    const [dietOptions, setDietOptions] = useState([]);
    const {currentUser} = useMyContext();

    useEffect(() => {
        const init = async () => {
            try {
                //Az összes opció lekérése
                const response = await api.get("/diet-option/list");
                const mappedDietOptions = response.data.map(item => ({
                    label: item.label,
                    value: item.apiValue,
                }));
                setDietOptions(mappedDietOptions);

                // A felhasználó mentett diétáinak betöltése, ha van
                if (currentUser) {
                    const dietResponse = await api.get("/diet-option/load-by-user");
                    const userDiets = dietResponse.data;

                    const defaultDietOptions = mappedDietOptions.filter(option =>
                        userDiets.includes(option.value)
                    );

                    onChange(defaultDietOptions);
                }
            } catch (error) {
                toast.error("There was an error while retrieving diet options");
            }
        };

        init();
    }, [currentUser, onChange]);


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