import Select from "react-select";
import {useEffect, useState} from "react";
import api from "../../Backend/api";


const IntoleranceSelect = ({onChange, value, placeholder}) => {
    const [intoleranceOptions, setIntoleranceOptions] = useState([]);

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

            } catch (error) {

            }
        }

        init();
    }, []);


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