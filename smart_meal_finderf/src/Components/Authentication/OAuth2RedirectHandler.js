import React, {useEffect} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {useMyContext} from "../../Store/ContextApi";
import toast from "react-hot-toast";
import {jwtDecode} from "jwt-decode";

const OAuth2RedirectHandler = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const {setToken, setIsAdmin} = useMyContext();

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const token = params.get("token");

        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                localStorage.setItem("JWT_TOKEN", token)

                const user = {
                    username: decodedToken.sub,
                    roles: decodedToken.roles.split(',')
                };
                localStorage.setItem("USER", JSON.stringify(user));

                //Context frissítése
                setToken(token);
                setIsAdmin(user.roles.includes("ADMIN"));

                //Átirányítás elcsúsztatása azért, hogy biztosan mentésre kerüljenek a változtatások
                setTimeout(() => {
                    console.log("Navigating to home page");
                    navigate("/");
                }, 100);

            } catch (error) {
                console.error("Token decoding failed: ", error);
                navigate("/login");
            }
        } else {
            toast.error("No token found");
            navigate("/login");
        }
    }, [location, navigate, setToken, setIsAdmin]);

    return <div>Redirecting...</div>;
};

export default OAuth2RedirectHandler;