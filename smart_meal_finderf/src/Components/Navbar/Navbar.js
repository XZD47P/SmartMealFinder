import React, {useEffect, useState} from "react";
import {Link, useLocation, useNavigate} from "react-router-dom";
import {useMyContext} from "../../Store/ContextApi";
import {RxCross2} from "react-icons/rx";
import {IoMenu} from "react-icons/io5";
import NavItem from "./NavItem";
import api from "../../Backend/api";


const Navbar = () => {
    const [headerToggle, setHeaderToggle] = useState(false);
    const pathName = useLocation().pathname;
    const navigate = useNavigate();

    const {token, setToken, setCurrentUser, isAdmin, setIsAdmin} = useMyContext();

    // Logout
    const handleLogout = async () => {
        try {
            await api.post("/auth/logout", null, {
                withCredentials: true
            })
        } catch (error) {
            console.log(error);
        } finally {
            localStorage.removeItem("JWT_TOKEN");
            localStorage.removeItem("USER");
            localStorage.removeItem("CSRF_TOKEN");
            localStorage.removeItem("IS_ADMIN");
            setToken(null);
            setCurrentUser(null);
            setIsAdmin(false);
            navigate("/login");
        }

    };

    useEffect(() => {
        setHeaderToggle(false);
    }, [pathName]);

    return (
        <header className="h-headerHeight z-50 text-textColor bg-headerColor shadow-sm flex items-center sticky top-0">
            <nav className="sm:px-10 px-4 flex w-full h-full items-center justify-between">
                <div className="flex items-center gap-6">
                    <Link to="/">
                        <h3 className="font-dancingScript text-logoText">Smart Meal Finder</h3>
                    </Link>

                    <div className="hidden lg:flex items-center gap-4">
                        <NavItem to="/whats-in-my-fridge">What's in my fridge</NavItem>

                        {token && (
                            <>
                                <NavItem to="/favourites">Favourites</NavItem>
                                <NavItem to="/plan">My Plan</NavItem>
                                <NavItem to="/weekly-meal">Meal Planner</NavItem>
                            </>
                        )}
                    </div>
                </div>

                {/* Mobile + right menu */}
                <ul
                    className={`lg:static absolute left-0 top-16 w-full lg:w-fit lg:px-0 sm:px-10 px-4 
                    lg:bg-transparent bg-headerColor
                    ${
                        headerToggle
                            ? "min-h-fit max-h-navbarHeight lg:py-0 py-4 shadow-md shadow-slate-700 lg:shadow-none"
                            : "h-0 overflow-hidden"
                    }
                    lg:h-auto transition-all duration-100 font-montserrat text-textColor 
                    flex lg:flex-row flex-col lg:gap-8 gap-2`}
                >
                    <NavItem to="/whats-in-my-fridge" className="lg:hidden">What's in my fridge</NavItem>
                    {token && (
                        <>
                            <NavItem to="/favourites" className="lg:hidden">Favourites</NavItem>
                            <NavItem to="/plan" className="lg:hidden">My Plan</NavItem>
                            <NavItem to="/weekly-meal" className="lg:hidden">Meal Planner</NavItem>
                        </>
                    )}
                    <NavItem to="/contact">Contact</NavItem>
                    <NavItem to="/about">About</NavItem>

                    {token ? (
                        <>
                            <NavItem to="/profile">Profile</NavItem>

                            {isAdmin && (
                                <NavItem to="/admin/users" className="uppercase">
                                    Admin
                                </NavItem>
                            )}

                            <button
                                onClick={handleLogout}
                                className="w-24 text-center bg-customRed font-semibold px-4 py-2 rounded-sm cursor-pointer hover:text-white"
                            >
                                LogOut
                            </button>
                        </>
                    ) : (
                        <NavItem
                            to="/login"
                            className="w-24 text-center bg-btnColor font-semibold px-4 py-2 rounded-sm hover:text-white"
                        >
                            Login
                        </NavItem>
                    )}
                </ul>
                <span
                    onClick={() => setHeaderToggle(!headerToggle)}
                    className="lg:hidden block cursor-pointer text-textColor shadow-md hover:text-white"
                >
                    {headerToggle ? <RxCross2 className="text-2xl"/> : <IoMenu className="text-2xl"/>}
                </span>
            </nav>
        </header>
    );
};

export default Navbar;
