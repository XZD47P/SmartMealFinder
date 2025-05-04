import axios from "axios";

const spoonacular = axios.create({
    baseURL: "https://api.spoonacular.com",
    headers: {
        "Content-Type": "application/json",
    }
});

//Api kulcs társítása minden híváshoz
spoonacular.interceptors.request.use(config => {
    const apiKey = process.env.REACT_APP_SPOONACULAR_API_KEY;
    config.params = {
        ...config.params || {},
        apiKey,
    };
    return config;
});

export default spoonacular;