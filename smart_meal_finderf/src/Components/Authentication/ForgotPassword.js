import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useMyContext} from "../../Store/ContextApi";
import {useForm} from "react-hook-form";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import Buttons from "../Utils/Buttons";
import InputField from "../Utils/InputField";

const ForgotPassword = () => {
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();
    const {token} = useMyContext();

    //React hook form létrehozása
    const {
        register,
        handleSubmit,
        reset,
        formState: {errors}
    } = useForm({
        defaultValues: {
            email: ""
        },
        mode: "onTouched",
    });

    const onPasswordForgotHandler = async (data) => {
        const {email} = data;

        try {
            setLoading(true);

            const formData = new URLSearchParams();
            formData.append("email", email);
            await api.post("/auth/public/forgot-password", formData, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            });
            reset();
            toast.success("Password reset email sent! Please check your inbox!");
        } catch (error) {
            toast.error("Error sending password reset email. Please try again later.")
        } finally {
            setLoading(false);
        }
    };

    //Ha a felhaszbáló be van jelentkezve, akkor irányítsuk vissza a LandingPage-re
    useEffect(() => {
        if (token) {
            navigate("/");
        }
    }, [token, navigate]);

    return (
        <div className="min-h-[calc(100vh-74px)] flex justify-center items-center">
            <form
                onSubmit={handleSubmit(onPasswordForgotHandler)}
                className="sm:w-[450px] w-[360px]  shadow-custom py-8 sm:px-8 px-4"
            >
                <div>
                    <h1 className="font-montserrat text-center font-bold text-2xl">
                        Forgot Password?
                    </h1>
                    <p className="text-slate-600 text-center">
                        Enter your email a Password reset email will sent
                    </p>
                </div>

                <div className="flex flex-col gap-2 mt-4">
                    <InputField
                        label="Email"
                        required
                        id="email"
                        type="email"
                        message="*Email is required"
                        placeholder="enter your email"
                        register={register}
                        errors={errors}
                    />{" "}
                </div>
                <Buttons
                    disabled={loading}
                    onClickhandler={() => {
                    }}
                    className="bg-customRed font-semibold text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-sm my-3"
                    type="text"
                >
                    {loading ? <span>Loading...</span> : "Send"}
                </Buttons>
                <p className=" text-sm text-slate-700 ">
                    <Link className=" underline hover:text-black" to="/login">
                        Back To Login
                    </Link>
                </p>
            </form>
        </div>
    )
};

export default ForgotPassword;