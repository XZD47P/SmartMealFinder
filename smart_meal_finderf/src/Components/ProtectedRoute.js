import React from "react";
import {Navigate} from 'react-router-dom';
import {useMyContext} from "../Store/ContextApi";

const ProtectedRoute = (children, adminPage) => {
    //JWT token és Admin jog ellenőrzése a ContextProviderből
    const {token, isAdmin} = useMyContext();

    //Átirányítás a LoginPage-re, ha nincs bejelentkezett felhasználó
    if (!token) {
        return <Navigate to="/login"/>
    }

    //Hozzáférés megtagadva, ha nem Admin
    if (token && adminPage && !isAdmin) {
        return <Navigate to="/access-denied"/>
    }
    return children;
}

export default ProtectedRoute;