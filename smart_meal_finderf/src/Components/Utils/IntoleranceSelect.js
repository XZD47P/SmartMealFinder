import Select from "react-select";
import {useEffect, useState} from "react";
import api from "../../Backend/api";
import {useMyContext} from "../../Store/ContextApi";
import toast from "react-hot-toast";


const IntoleranceSelect = ({onChange, value, placeholder}) => {
    const [intoleranceOptions, setIntoleranceOptions] = useState([]);
    const {currentUser} = useMyContext();

    useEffect(() => {
        const init = async () => {
            try {
                //Az összes opció lekérése
                const response = await api.get("/intolerance/list");
                const mappedDietOptions = response.data.map(item => ({
                    label: item.label,
                    value: item.apiValue,
                }));
                setIntoleranceOptions(mappedDietOptions);

                // A felhasználó mentett intoleranciáinak betöltése, ha van
                if (currentUser) {
                    const intoleranceResponse = await api.get("/intolerance/load-by-user");
                    const userIntolerances = intoleranceResponse.data;

                    const defaultIntoleranceOptions = mappedDietOptions.filter(option =>
                        userIntolerances.includes(option.value)
                    );

                    onChange(defaultIntoleranceOptions);
                }
                ;
            } catch (error) {
                toast.error("There was an error while retrieving food intolerance options");
            }
        };

        init();
    }, [currentUser, onChange]);


    return (
        <div className="mb-4">
            <label className="font-medium">Do you have any dietary restriction?</label>
            <Select
                isMulti
                options={intoleranceOptions}
                onChange={onChange}
                value={value}
                placeholder={placeholder}
                className="mt-2"
            />
        </div>
    );
};

export default IntoleranceSelect;