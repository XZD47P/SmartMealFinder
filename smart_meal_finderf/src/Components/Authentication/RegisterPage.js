import {useEffect, useState} from "react";
import {useMyContext} from "../../Store/ContextApi";
import {Link, useNavigate} from "react-router-dom";
import {useForm} from "react-hook-form";
import Buttons from "../Utils/Buttons";
import InputField from "../Utils/InputField";
import {FaGithub} from "react-icons/fa";
import {FcGoogle} from "react-icons/fc";
import api from "../../Backend/api";
import toast from "react-hot-toast";

const RegisterPage = () => {
    const apiUrl = process.env.REACT_APP_API_URL;
    const [role, setRole] = useState();
    const [loading, setLoading] = useState(false)
    const {token} = useMyContext();
    const navigate = useNavigate();

    //React Hook form létrehozása
    const {
        register,
        handleSubmit,
        reset,
        setError,
        formState: {errors},
    } = useForm({
        defaultValues: {
            firstName: "",
            lastName: "",
            username: "",
            email: "",
            password: "",
        },
        mode: "onTouched",
    });

    useEffect(() => {
        setRole("user");
    }, []);

    const onSubmitHandler = async (data) => {
        const {firstName, lastName, username, email, password} = data;
        const sendData = {
            firstName,
            lastName,
            username,
            email,
            password,
            role: [role],
        };

        try {
            setLoading(true);
            const response = await api.post("/auth/public/register", sendData);
            toast.success("Registration was successful! Please check your email to complete the registration!");
            reset();
            if (response.data) {
                navigate("/login");
            }
        } catch (error) {
            //setError(keyword, message) = keyword a mező neve, ahova a hibaüzenetet írom
            if (error?.response?.data?.message === "Error: Username is already taken!") {
                setError("username", {message: "Username is already taken!"});
            } else if (error?.response?.data?.message === "Error: Email is already in use!") {
                setError("email", {message: "Email is already in use!"});
            }
        } finally {
            setLoading(false);
        }
    };
    useEffect(() => {
        if (token) {
            navigate("/");
        }
    }, [navigate, token]);

    return (
        <div className="min-h-[calc(100vh-74px)] flex justify-center items-center">
            <form
                onSubmit={handleSubmit(onSubmitHandler)}
                className="sm:w-[450px] w-[360px]  shadow-custom py-6 sm:px-8 px-4 bg-white"
            >
                <div>
                    <h1 className="font-montserrat text-center font-bold text-2xl">
                        Register Here Here
                    </h1>
                    <p className="text-slate-600 text-center">
                        Enter your credentials to create new account
                    </p>
                    <div className="flex items-center justify-between gap-1 py-5 ">
                        <a
                            href={`${apiUrl}/oauth2/authorization/google`}
                            className="flex gap-1 items-center justify-center flex-1 border p-2 shadow-sm shadow-slate-200 rounded-md hover:bg-slate-300 transition-all duration-300"
                        >
              <span>
                <FcGoogle className="text-2xl"/>
              </span>
                            <span className="font-semibold sm:text-customText text-xs">
                Login with Google
              </span>
                        </a>
                        <a
                            href={`${apiUrl}/oauth2/authorization/github`}
                            className="flex gap-1 items-center justify-center flex-1 border p-2 shadow-sm shadow-slate-200 rounded-md hover:bg-slate-300 transition-all duration-300"
                        >
                            <span>
                                 <FaGithub className="text-2xl"/>
                            </span>
                            <span className="font-semibold sm:text-customText text-xs">
                            Login with Github
                            </span>
                        </a>
                    </div>
                </div>

                <div className="flex flex-col gap-2">
                    <InputField
                        label="First name"
                        required
                        id="firstName"
                        type="text"
                        message="*First name is required"
                        placeholder="type your first name"
                        register={register}
                        errors={errors}
                    />
                    <InputField
                        label="Last name"
                        required
                        id="lastName"
                        type="text"
                        message="*Last name is required"
                        placeholder="type your last name"
                        register={register}
                        errors={errors}
                    />
                    <InputField
                        label="Username"
                        required
                        id="username"
                        type="text"
                        message="*UserName is required"
                        placeholder="type your username"
                        register={register}
                        errors={errors}
                    />
                    <InputField
                        label="Email"
                        required
                        id="email"
                        type="email"
                        message="*Email is required"
                        placeholder="type your email"
                        register={register}
                        errors={errors}
                    />
                    <InputField
                        label="Password"
                        required
                        id="password"
                        type="password"
                        message="*Password is required"
                        placeholder="type your password"
                        register={register}
                        errors={errors}
                        min={6}
                    />
                </div>
                <Buttons
                    disabled={loading}
                    onClickhandler={() => {
                    }}
                    className="bg-customRed font-semibold flex justify-center text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-sm my-3"
                    type="text"
                >
                    {loading ? <span>Loading...</span> : "Register"}
                </Buttons>

                <p className="text-center text-sm text-slate-700 mt-2">
                    Already have an account?{" "}
                    <Link
                        className="font-semibold underline hover:text-black"
                        to="/login"
                    >
                        Login
                    </Link>
                </p>
            </form>
        </div>
    );
}

export default RegisterPage;