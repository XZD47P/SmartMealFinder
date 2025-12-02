import './App.css';
import React from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import {Toaster} from "react-hot-toast";
import Navbar from "./Components/Navbar"
import ContactPage from "./Components/ContactPage";
import AboutPage from "./Components/AboutPage";
import LoginPage from "./Components/Authentication/LoginPage";
import RegisterPage from "./Components/Authentication/RegisterPage";
import LandingPage from "./Components/LandingPage";
import OAuth2RedirectHandler from "./Components/Authentication/OAuth2RedirectHandler";
import ForgotPassword from "./Components/Authentication/ForgotPassword";
import ResetPassword from "./Components/Authentication/ResetPassword";
import AccessDenied from "./Components/Authentication/AccessDenied";
import ProtectedRoute from "./Components/ProtectedRoute";
import UserProfile from "./Components/Authentication/UserProfile";
import Admin from "./Components/Admin/Admin";
import VerificationPage from "./Components/Authentication/VerificationPage";
import Plan from "./Components/DietPlan/Plan";
import RecipeDetailPage from "./Components/Recipe/RecipeDetailPage";
import WhatsInMyFridgePage from "./Components/Recipe/WhatsInMyFridgePage";
import FavouriteRecipesPage from "./Components/Recipe/FavouriteRecipesPage";

const App = () => {
    return (
        <Router>
            <Navbar/>
            <Toaster position={"bottom-center"} reverseOrder={false}/>
            <Routes>
                <Route path="/" element={<LandingPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/contact" element={<ContactPage/>}/>
                <Route path="/about" element={<AboutPage/>}/>
                <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler/>}/>
                <Route path="/forgot-password" element={<ForgotPassword/>}/>
                <Route path="/reset-password" element={<ResetPassword/>}/>
                <Route path="/access-denied" element={<AccessDenied/>}/>
                <Route path="/verification" element={<VerificationPage/>}/>
                <Route path="/recipes/:id" element={<RecipeDetailPage/>}/>
                <Route path="/whats-in-my-fridge" element={<WhatsInMyFridgePage/>}/>
                <Route
                    path="/profile"
                    element={
                        <ProtectedRoute>
                            <UserProfile/>
                        </ProtectedRoute>
                    }
                />
                <Route path="/plan" element={
                    <ProtectedRoute>
                        <Plan/>
                    </ProtectedRoute>
                }
                />
                <Route
                    path="/admin/*"
                    element={
                        <ProtectedRoute adminPage={true}>
                            <Admin/>
                        </ProtectedRoute>
                    }
                />
                <Route
                    path="/favourites"
                    element={
                        <ProtectedRoute>
                            <FavouriteRecipesPage/>
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </Router>
    )
}

export default App;
