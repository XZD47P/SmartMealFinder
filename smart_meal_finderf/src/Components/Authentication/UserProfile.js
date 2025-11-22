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
import DietOptionSelect from "../Utils/DietOptionSelect";
import IntoleranceSelect from "../Utils/IntoleranceSelect";
import ConfirmModal from "../Utils/ConfirmModal";


const UserProfile = () => {
    //Bejelentkezett felhasználó és token megszerzése
    const {currentUser, token, fetchUser} = useMyContext();
    const [loginSession, setLoginSession] = useState(null);

    const [pageError, setPageError] = useState(null);

    const [openAccountCredential, setopenAccountCredential] = useState(false);
    const [openSetting, setOpenSetting] = useState(false);
    const [loading, setLoading] = useState(false);
    const [pageLoader, setPageLoader] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [nextCheckedValue, setNextCheckedValue] = useState(null);

    //Diéták mentése
    const [userDiets, setUserDiets] = useState([]);
    const handleDietChange = async () => {
        try {
            setLoading(true);
            await api.post("/diet-option/save-to-user", {
                diets: userDiets.map((diet) => diet.value),
            });
            toast.success("Diet added successfully.");
        } catch (error) {
            toast.error("Failed to add diet to user. Please try again later.");
        } finally {
            setLoading(false);
        }
    };

    //Intolernaciák mentése
    const [userIntolerance, setUserIntolerance] = useState([]);
    const handleIntoleranceChange = async () => {
        try {
            setLoading(true);
            await api.post("/intolerance/save-to-user", {
                intolerances: userIntolerance.map((intolerance) => intolerance.value),
            });
            toast.success("Intolerance added successfully.");
        } catch (error) {
            toast.error("Failed to add intolerance to user. Please try again.");
        } finally {
            setLoading(false);
        }
    };

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
            const formData = {
                token,
                oldPassword,
                newPassword,
                newPassword2
            }
            await api.post("/auth/change-password", formData, {
                headers: {
                    "Content-Type": "application/json",
                }
            });
            toast.success("Password changed successfully!")
        } catch (error) {
            toast.error("Password change failed!");
        } finally {
            setLoading(false);
        }
    };

    // const handleCheckboxChange = async (e) => {
    //     try {
    //         setLoading(true)
    //         await api.put("/auth/user/update-profiling-status", {checked: e.target.checked});
    //         toast.success("Profiling status successfully updated!");
    //         fetchUser();
    //         setLoading(false);
    //     } catch (error) {
    //         toast.error("Something went wrong, please try again!");
    //     } finally {
    //         setLoading(false);
    //     }
    // };

    const onCheckboxClick = (e) => {
        const newValue = e.target.checked;

        // Engedélyezéskor egyből engedjük
        if (currentUser?.profilingEnabled === false && newValue === true) {
            confirmProfilingChange(true);
            return;
        }

        // Tiltáskor kérdezzünk rá
        if (currentUser?.profilingEnabled === true && newValue === false) {
            setNextCheckedValue(false);
            setConfirmOpen(true);
        }
    };


    const confirmProfilingChange = async (value = nextCheckedValue) => {
        setConfirmOpen(false);

        try {
            setLoading(true);

            await api.put("/auth/user/update-profiling-status", {checked: value});

            toast.success("Profiling status successfully updated!");
            fetchUser();
        } catch (error) {
            toast.error("Something went wrong, please try again!");
        } finally {
            setLoading(false);
            setNextCheckedValue(null);
        }
    };

    const cancelProfilingChange = () => {
        setConfirmOpen(false);
        setNextCheckedValue(null);
    };


    //A jelenlegi felhasználó adatainak betöltése az input fieldekbe
    useEffect(() => {
        if (currentUser?.id) {
            setValue("username", currentUser.username);
            setValue("email", currentUser.email);
        }
    }, [currentUser, setValue]);


    useEffect(() => {
        if (token) {
            const decodedToken = jwtDecode(token);

            const lastLogin = moment.unix(decodedToken.iat).format("YYYY-MM-DD, HH:mm:ss");
            setLoginSession(lastLogin);
        }
    }, [token]);

    //Hibakezelés
    if (pageError) {
        return <Errors message={pageError}/>;
    }

    //Kinyitott fiók fül
    const onOpenAccountCredentialsHandler = () => {
        setopenAccountCredential(!openAccountCredential);
        setOpenSetting(false);
    };
    //kinyitott beállítások fül
    const onOpenSettingHandler = () => {
        setOpenSetting(!openSetting);
        setopenAccountCredential(false);
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
                        className="xl:w-[70%] lg:w-[80%] sm:w-[90%] w-full sm:mx-auto sm:px-0 px-4   min-h-[500px] flex lg:flex-row flex-col gap-4 bg-white">
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
                                    <Accordion expanded={openAccountCredential}>
                                        <AccordionSummary
                                            className="shadow-md shadow-gray-300"
                                            onClick={onOpenAccountCredentialsHandler}
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
                                                    placeholder="Enter your current password"
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
                                                <DietOptionSelect value={userDiets}
                                                                  onChange={setUserDiets}
                                                                  placeholder={"Select your diets"}/>
                                                <Buttons disabled={loading}
                                                         type={"button"}
                                                         className={"bg-green-600 text-white w-full mt-4 py-2 rounded hover:bg-green-700 transition-colors"}
                                                         onClickhandler={handleDietChange}>Save</Buttons>
                                                <IntoleranceSelect value={userIntolerance}
                                                                   onChange={setUserIntolerance}
                                                                   placeholder={"Select your intolerance"}/>
                                                <Buttons disabled={loading}
                                                         type={"button"}
                                                         className={"bg-green-600 text-white w-full mt-4 py-2 rounded hover:bg-green-700 transition-colors"}
                                                         onClickhandler={handleIntoleranceChange}>Save</Buttons>
                                                <div className="flex items-center gap-2 mt-6">
                                                    <label className="text-slate-600 text-sm font-semibold uppercase">
                                                        {" "}
                                                        Profiling Enabled
                                                    </label>
                                                    <input
                                                        className="text-14 w-5 h-5"
                                                        type="checkbox"
                                                        name="profiling"
                                                        checked={currentUser?.profilingEnabled}
                                                        onChange={(e) => onCheckboxClick(e)}
                                                    />
                                                    <ConfirmModal
                                                        open={confirmOpen}
                                                        onConfirm={confirmProfilingChange}
                                                        onCancel={cancelProfilingChange}
                                                    />
                                                </div>
                                            </AccordionDetails>
                                        </Accordion>
                                    </div>
                                    <div className="pt-10 ">
                                        <h3 className="text-slate-800 text-lg font-semibold  mb-2 px-2">
                                            Last Login Session
                                        </h3>
                                        <div className="shadow-md shadow-gray-300 px-4 py-2 rounded-md">
                                            <p className="text-slate-700 text-sm">
                                                Last time you logged in: <br/>
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