import {useMyContext} from "../../Store/ContextApi";
import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import moment from "moment";
import {jwtDecode} from "jwt-decode";
import Errors from "../Errors";
import {Accordion, AccordionDetails, AccordionSummary} from "@mui/material";
import Buttons from "../Utils/Buttons";
import InputField from "../Utils/InputField";
import Avatar from "@mui/material/Avatar";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import {ClockLoader} from "react-spinners";


const UserProfile = () => {
    //Bejelentkezett felhasználó és token megszerzése
    const {currentUser, token} = useMyContext();
    const [loginSession, setLoginSession] = useState(null);

    const [pageError, setPageError] = useState(null);

    const [accountLocked, setAccountLocked] = useState();
    const [accountVerified, setAccountVerified] = useState();
    const [verificationDeadline, setVerificationDeadline] = useState();

    const [openAccount, setOpenAccount] = useState(false);
    const [openSetting, setOpenSetting] = useState(false);
    const [loading, setLoading] = useState(false);
    const [pageLoader, setPageLoader] = useState(false);


    const {
        register,
        handleSubmit,
        setValue,
        getValues,
        formState: {errors},
    } = useForm({
        defaultValues: {
            oldPassword: "",
            newPassword: "",
            newPassword2: ""
        },
        mode: "onTouched",
    });

    const handleChangePassword = async (data) => {
        const oldPassword = data.oldPassword;
        const newPassword = data.newPassword;
        const newPassword2 = data.newPassword2;

        try {
            setLoading(true);
            const formData = new URLSearchParams();
            formData.append("token", token);
            formData.append("oldPassword", oldPassword);
            formData.append("newPassword", newPassword);
            formData.append("newPassword2", newPassword2);
            await api.post("/auth/change-password", formData, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                }
            });
            toast.success("Password changed successfully!")
        } catch (error) {
            toast.error("Password change failed!");
        } finally {
            setLoading(false);
        }
    };

    //A jelenlegi felhasználó adatainak betöltése az input fieldekbe
    useEffect(() => {
        if (currentUser?.id) {
            setValue("username", currentUser.username);
            setValue("email", currentUser.email);
            setAccountLocked(!currentUser.accountNonLocked);
            setAccountVerified(currentUser.accountVerified)

            const accountVerificationDate = moment(currentUser?.verificationDeadline).format("YYYY-MM-DD");
            setVerificationDeadline(accountVerificationDate);
        }
    }, [currentUser, setValue]);


    useEffect(() => {
        if (token) {
            const decodedToken = jwtDecode(token);

            const lastLogin = moment.unix(decodedToken.iat).format("YYYY-MM-DD, HH:mm:ss");
            setLoginSession(lastLogin);
        }
    }, [token]);

    // const handleAccountLockStatus = async (event) => {
    //     setAccountLocked(event.target.checked);
    //
    //     try {
    //         const formData = new URLSearchParams();
    //         formData.append("token", token);
    //         formData.append("lock", event.target.checked);
    //
    //         await api.put("/auth/update-lock-status", formData, {
    //             headers: {
    //                 "Content-Type": "application/x-www-form-urlencoded",
    //             },
    //         });
    //
    //         //fetchUser();
    //         toast.success("Update Account Lock Status");
    //     } catch (error) {
    //         toast.error("Update Account Lock status failed");
    //     } finally {
    //         setLoading(false);
    //     }
    // };

    //Hibakezelés
    if (pageError) {
        return <Errors message={pageError}/>;
    }

    //Kinyitott fiók fül
    const onOpenAccountHandler = () => {
        setOpenAccount(!openAccount);
        setOpenSetting(false);
    };
    //kinyitott beállítások fül
    const onOpenSettingHandler = () => {
        setOpenSetting(!openSetting);
        setOpenAccount(false);
    };

    return (
        <div className="min-h-[calc(100vh-74px)] py-10">
            {pageLoader ? (
                <>
                    {" "}
                    <div className="flex  flex-col justify-center items-center  h-72">
            <span>
              <ClockLoader
                  color="green"
                  loading={loading}
                  size={50}
                  aria-label="Loading Spinner"
                  data-testid="loader"
              />
            </span>
                        <span>Please wait...</span>
                    </div>
                </>
            ) : (
                <>
                    {" "}
                    <div
                        className="xl:w-[70%] lg:w-[80%] sm:w-[90%] w-full sm:mx-auto sm:px-0 px-4   min-h-[500px] flex lg:flex-row flex-col gap-4 ">
                        <div className="flex-1  flex flex-col shadow-lg shadow-gray-300 gap-2 px-4 py-6">
                            <div className="flex flex-col items-center gap-2   ">
                                <Avatar
                                    alt={currentUser?.username}
                                    src="/static/images/avatar/1.jpg"
                                />
                                <h3 className="font-semibold text-2xl">
                                    {currentUser?.username}
                                </h3>
                            </div>
                            <div className="my-4 ">
                                <div className="space-y-2 px-4 mb-1">
                                    <h1 className="font-semibold text-md text-slate-800">
                                        UserName :{" "}
                                        <span className=" text-slate-700  font-normal">
                      {currentUser?.username}
                    </span>
                                    </h1>
                                    <h1 className="font-semibold text-md text-slate-800">
                                        Role :{" "}
                                        <span className=" text-slate-700  font-normal">
                      {currentUser && currentUser["roles"][0]}
                    </span>
                                    </h1>
                                </div>
                                <div className="py-3">
                                    <Accordion expanded={openAccount}>
                                        <AccordionSummary
                                            className="shadow-md shadow-gray-300"
                                            onClick={onOpenAccountHandler}
                                            expandIcon={<ArrowDropDownIcon/>}
                                            aria-controls="panel1-content"
                                            id="panel1-header"
                                        >
                                            <h3 className="text-slate-800 text-lg font-semibold ">
                                                Update your login credentials
                                            </h3>
                                        </AccordionSummary>
                                        <AccordionDetails className="shadow-md shadow-gray-300">
                                            <form
                                                className=" flex flex-col gap-3"
                                                onSubmit={handleSubmit(handleChangePassword)}
                                            >
                                                <InputField
                                                    label="Old password"
                                                    required
                                                    id="oldPassword"
                                                    className="text-sm"
                                                    type="password"
                                                    message="*Old password is required"
                                                    placeholder="Enter your username"
                                                    register={register}
                                                    errors={errors}
                                                />{" "}
                                                <InputField
                                                    label="Enter New Password"
                                                    id="newPassword"
                                                    className="text-sm"
                                                    type="password"
                                                    message="*Password is required"
                                                    placeholder="type your password"
                                                    register={register}
                                                    errors={errors}
                                                    min={6}
                                                />{" "}
                                                <InputField
                                                    label="Enter new password again"
                                                    id="newPassword2"
                                                    className="text-sm"
                                                    type="password"
                                                    message="*Password is required"
                                                    placeholder="type your password"
                                                    register={register}
                                                    errors={errors}
                                                    min={6}
                                                    validation={{
                                                        validate: (value) =>
                                                            value === getValues("newPassword") || "*Passwords do not match",
                                                    }}
                                                />
                                                <Buttons
                                                    disabled={loading}
                                                    className="bg-customRed font-semibold flex justify-center text-white w-full py-2 hover:text-slate-400 transition-colors duration-100 rounded-sm my-3"
                                                    type="submit"
                                                >
                                                    {loading ? <span>Loading...</span> : "Update"}
                                                </Buttons>
                                            </form>
                                        </AccordionDetails>
                                    </Accordion>
                                    <div className="mt-6">
                                        <Accordion expanded={openSetting}>
                                            <AccordionSummary
                                                className="shadow-md shadow-gray-300"
                                                onClick={onOpenSettingHandler}
                                                expandIcon={<ArrowDropDownIcon/>}
                                                aria-controls="panel1-content"
                                                id="panel1-header"
                                            >
                                                <h3 className="text-slate-800 text-lg font-semibold">
                                                    Account Setting
                                                </h3>
                                            </AccordionSummary>
                                            <AccordionDetails className="shadow-md shadow-gray-300">

                                            </AccordionDetails>
                                        </Accordion>
                                    </div>
                                    <div className="pt-10 ">
                                        <h3 className="text-slate-800 text-lg font-semibold  mb-2 px-2">
                                            Last Login Session
                                        </h3>
                                        <div className="shadow-md shadow-gray-300 px-4 py-2 rounded-md">
                                            <p className="text-slate-700 text-sm">
                                                Your Last LogIn Session when you are loggedin <br/>
                                                <span>{loginSession}</span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
}

export default UserProfile;