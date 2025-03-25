import React from "react";
import {Link} from "react-router-dom";
import {FaFacebookF, FaInstagram, FaLinkedinIn, FaTwitter,} from "react-icons/fa";
//import aboutImage from "./path/to/your/image.jpg"; // Add your image path here

const AboutPage = () => {
    return (
        <div className=" p-8   bg-gray-100 min-h-screen">
            <div className="md:w-1/2">
                <h1 className="text-4xl font-bold mb-4">About Us</h1>
                <p className="mb-4">
                    Welcome to Smart Meal Finder! This project was mainly built to be my Thesis project.
                    I try to make the world a healthier place by supporting your eating habits with this website.
                    <br/>You can:
                </p>

                <ul className="list-disc list-inside mb-4 text-sm px-6 py-2">
                    <li className="mb-2">
                        Find recipes based on what's in your fridge or by their name.
                    </li>
                    <li className="mb-2">
                        Get personal recommendations based on your profile.
                    </li>
                    <li className="mb-2">
                        If you have any kind of diatery restriction, we won't show you food you can't eat.
                    </li>
                    <li className="mb-2">
                        Set up a plan with a goal if you're trying to lose weight, or gain weight. It is up to you!
                    </li>
                </ul>
                <div className="flex space-x-4 mt-10">
                    <Link className="text-white rounded-full p-2 bg-customRed  " to="/">
                        <FaFacebookF size={24}/>
                    </Link>
                    <Link className="text-white rounded-full p-2 bg-customRed  " to="/">
                        <FaTwitter size={24}/>
                    </Link>
                    <Link className="text-white rounded-full p-2 bg-customRed  " to="/">
                        <FaLinkedinIn size={24}/>
                    </Link>
                    <Link className="text-white rounded-full p-2 bg-customRed  " to="/">
                        <FaInstagram size={24}/>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default AboutPage;
