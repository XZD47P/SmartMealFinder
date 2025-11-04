import {createContext, useContext, useEffect, useState} from "react";
import api from "../Backend/api";
import toast from "react-hot-toast";

const ContextApi = createContext();

export const ContextProvider = ({children}) => {
    //token megkeresése a lokalstorage-ban
    const getToken = localStorage.getItem("JWT_TOKEN")
        ? JSON.stringify(localStorage.getItem("JWT_TOKEN"))
        : null;

    //Admin jogosultág megvizsgálása
    const isADmin = localStorage.getItem("IS_ADMIN")
        ? JSON.stringify(localStorage.getItem("IS_ADMIN"))
        : null;

    //Token tárolása
    const [token, setToken] = useState(getToken);

    //Bejelentkezett felhasználó tárolása
    const [currentUser, setCurrentUser] = useState(null);

    // //Oldalsó menű nyitva állapota
    // const [openSidebar, setOpenSidebar] = useState(true);

    //Admin jogosultság meglétének ellenőrzése
    const [isAdmin, setIsAdmin] = useState(isADmin);

    //User betöltése
    const fetchUser = async () => {
        const user = JSON.parse(localStorage.getItem("USER"));

        if (user?.username) {
            try {
                const {data} = await api.get("/auth/user");
                const roles = data.roles;

                if (roles.includes("ROLE_ADMIN")) {
                    localStorage.setItem("IS_ADMIN", JSON.stringify(true));
                    setIsAdmin(true);
                } else {
                    localStorage.removeItem("IS_ADMIN");
                    setIsAdmin(false);
                }
                setCurrentUser(data);
            } catch (error) {
                console.log("Error fetching current user: ", error);
                toast.error("Error fetching current user");
            }
        }
    };

    const [refreshProgress, setRefreshProgress] = useState(false);

    const triggerProgressRefresh = () => {
        setRefreshProgress(prev => !prev);
    }

    //Ha a token létezik, akkor keressük ki a felhasználót
    useEffect(() => {
        if (token) {
            fetchUser();
        }
    }, [token]);

    //A ContextProvideren keresztűl mindehol elérhető tesszük a felhasználói adatokat
    return (
        <ContextApi.Provider
            value={{
                token,
                setToken,
                currentUser,
                setCurrentUser,
                // openSidebar,
                // setOpenSidebar,
                isAdmin,
                setIsAdmin,
                refreshProgress,
                triggerProgressRefresh,
            }}
        >
            {children}
        </ContextApi.Provider>
    );
};

//useMyContext egyedi hook segítségével bárhol meghívható
export const useMyContext = () => {
    const context = useContext(ContextApi);

    return context;
}