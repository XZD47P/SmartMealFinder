import {useEffect, useState} from "react";
import {Link, useSearchParams} from "react-router-dom";
import api from "../../Backend/api";
import toast from "react-hot-toast";


const VerificationPage = () => {
    const [success, setSuccess] = useState(null);
    const [loading, setLoading] = useState(false);
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");


    useEffect(() => {
        async function verifyUser() {
            setLoading(true);
            try {
                const data = new URLSearchParams();

                data.append("token", token);
                await api.post("/auth/public/verify-user", data, {
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    }
                })

                setSuccess(true);
                setLoading(false);
            } catch (error) {
                toast.error("Verification failed, please try again");
                setLoading(false);
            }
        }

        verifyUser();
    }, [token]);

    return (
        <div className="min-h-[calc(100vh-74px)] flex justify-center items-center">
            <div className="text-center">
                {loading ? (
                    <p className="text-xl text-slate-600">Verifying your account...</p>
                ) : (
                    <>
                        <h1 className="text-2xl font-bold">
                            {success ? "Verification was successful!" : "Verification failed!"}
                        </h1>
                        <p className="text-slate-600 mt-2 mb-4">
                            {success
                                ? "Thank you for confirming your account. You may now log in."
                                : "Please try again, or contact support if the issue persists."}
                        </p>
                        <Link className="underline text-blue-600 hover:text-blue-800" to="/login">
                            Back To Login
                        </Link>
                    </>
                )}
            </div>
        </div>
    );
}

export default VerificationPage;