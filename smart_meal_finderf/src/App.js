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
            </Routes>
        </Router>
    )
}

export default App;
