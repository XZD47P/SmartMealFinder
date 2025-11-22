import {useForm} from "react-hook-form";
import {useCallback, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import Errors from "../Errors";
import {ClockLoader} from "react-spinners";
import InputField from "../Utils/InputField";
import Buttons from "../Utils/Buttons";

const UserDetails = () => {

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

    const [loading, setLoading] = useState(false);
    const [updateRoleLoader, setUpdateRoleLoader] = useState(false);
    const [passwordLoader, setPasswordLoader] = useState(false);

    const {userId} = useParams();
    const [user, setUser] = useState(null);
    const [roles, setRoles] = useState([]);
    const [selectedRole, setSelectedRole] = useState("");
    const [error, setError] = useState(null);
    const [isEditingPassword, setIsEditingPassword] = useState(false);

    //User kikeresése
    const fetchUserDetails = useCallback(async () => {
        setLoading(true);
        try {
            const response = await api.get(`/admin/user/${userId}`);
            setUser(response.data);

            setSelectedRole(response.data.role?.roleName || "");
        } catch (error) {
            setError(error.response?.data?.error?.message);
            toast.error("Error fetching user data", error);
        } finally {
            setLoading(false);
        }
    }, [userId]);

    useEffect(() => {
        //Ha megvan a user, adatainak betöltése a felületre
        if (user && Object.keys(user).length > 0) {
            setValue("username", user.username);
            setValue("email", user.email);
        }
    }, [user, setValue]);

    const fetchRoles = useCallback(async () => {
        try {
            const response = await api.get("/admin/roles");
            setRoles(response.data);
        } catch (error) {
            setError(error.response?.data?.message);
            toast.error("Error fetching roles", error);
        }
    }, []);

    useEffect(() => {
        fetchUserDetails();
        fetchRoles();
    }, [fetchUserDetails, fetchRoles]);

    //Rang kiválasztásának kezelése
    const handleRoleChange = (e) => {
        setSelectedRole(e.target.value);
    }

    const handleUpdateRole = async () => {
        setUpdateRoleLoader(true);
        try {
            const formData = {
                userId,
                role: selectedRole,
            }

            await api.put("/admin/update-role", formData, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            fetchUserDetails();
            toast.success("Successfully changed role!");
        } catch (error) {
            toast.error("Role changed failed");
        } finally {
            setUpdateRoleLoader(false);
        }
    }

    //Jelszócsere kezelése
    const handleSavePassword = async (data) => {
        setPasswordLoader(true);
        const newPassword = data.password;

        try {
            const formData = {
                userId,
                password: newPassword,
            };

            await api.put("/admin/update-password", formData, {
                headers: {
                    "Content-Type": "application/json",
                }
            });
            setIsEditingPassword(false);
            setValue("password", "");

            toast.success("Successfully changed password!");
        } catch (error) {
            toast.error("Password changed failed!");
        } finally {
            setPasswordLoader(false);
        }
    }

    const handleCheckboxChange = async (e, updateUrl) => {
        const {name, checked} = e.target;

        let message = null;
        if (name === "lock") {
            message = "Account lock status successfully updated";
        } else if (name === "verify") {
            message = "Account verification status successfully updated";
        }
        switch (name) {
            case "lock":
                message = "Account lock status successfully updated";
                break;
            case "verify":
                message = "Account verification status successfully updated";
                break;
            case "profiling":
                message = "Account profiling status successfully updated";
                break;
        }

        try {
            const formData = {
                userId,
                checked: checked,
            };

            await api.put(updateUrl, formData, {
                headers: {
                    "Content-Type": "application/json",
                }
            })
            fetchUserDetails();
            toast.success(message);
        } catch (error) {
            toast.error("Something went wrong, please try again!");
        } finally {
            message = null;
        }
    };

    if (error) {
        return <Errors message={error}/>
    }
    // TODO: Megcsinálni a teljes oldalt
    return (
        <div className="sm:px-12 px-4 py-10   ">
            {loading ? (
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
                    <div
                        className="lg:w-[70%] sm:w-[90%] w-full  mx-auto shadow-lg shadow-gray-300 p-8 rounded-md bg-white">
                        <div>
                            <h1 className="text-slate-800 text-2xl font-bold  pb-4">
                                Profile Information
                                <hr/>
                            </h1>
                            <form
                                className="flex  flex-col  gap-2  "
                                onSubmit={handleSubmit(handleSavePassword)}
                            >
                                <InputField
                                    label="UserName"
                                    required
                                    id="username"
                                    className="w-full"
                                    type="text"
                                    message="*UserName is required"
                                    placeholder="Enter your UserName"
                                    register={register}
                                    errors={errors}
                                    readOnly
                                />
                                <InputField
                                    label="Email"
                                    required
                                    id="email"
                                    className="flex-1"
                                    type="text"
                                    message="*Email is required"
                                    placeholder="Enter your Email"
                                    register={register}
                                    errors={errors}
                                    readOnly
                                />
                                <InputField
                                    label="Password"
                                    required
                                    autoFocus={isEditingPassword}
                                    id="password"
                                    className="w-full"
                                    type="password"
                                    message="*Password is required"
                                    placeholder="Enter your Password"
                                    register={register}
                                    errors={errors}
                                    readOnly={!isEditingPassword}
                                    min={6}
                                />{" "}
                                {!isEditingPassword ? (
                                    <Buttons
                                        type="button"
                                        onClickhandler={() =>
                                            setIsEditingPassword(!isEditingPassword)
                                        }
                                        className="bg-customRed mb-0 w-fit px-4 py-2 rounded-md text-white"
                                    >
                                        Click To Edit Password
                                    </Buttons>
                                ) : (
                                    <div className="flex items-center gap-2 ">
                                        <Buttons
                                            type="submit"
                                            className="bg-btnColor mb-0 w-fit px-4 py-2 rounded-md text-white"
                                        >
                                            {passwordLoader ? "Loading.." : "Save"}
                                        </Buttons>
                                        <Buttons
                                            type="button"
                                            onClickhandler={() =>
                                                setIsEditingPassword(!isEditingPassword)
                                            }
                                            className="bg-customRed mb-0 w-fit px-4 py-2 rounded-md text-white"
                                        >
                                            Cancel
                                        </Buttons>
                                    </div>
                                )}
                            </form>
                        </div>
                    </div>
                    <div
                        className="lg:w-[70%] sm:w-[90%] w-full  mx-auto shadow-lg shadow-gray-300 p-8 rounded-md bg-white">
                        <h1 className="text-slate-800 text-2xl font-bold  pb-4">
                            Admin Actions
                            <hr/>
                        </h1>

                        <div className="py-4 flex sm:flex-row flex-col sm:items-center items-start gap-4">
                            <div className="flex items-center gap-2">
                                <label className="text-slate-600 text-lg font-semibold ">
                                    Role:{" "}
                                </label>
                                <select
                                    className=" px-8 py-1 rounded-md  border-2 uppercase border-slate-600  "
                                    value={selectedRole}
                                    onChange={handleRoleChange}
                                >
                                    {roles.map((role) => (
                                        <option
                                            className="bg-slate-200 flex flex-col gap-4 uppercase text-slate-700"
                                            key={role.roleId}
                                            value={role.roleName}
                                        >
                                            {role.roleName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <button
                                className="bg-btnColor hover:text-slate-300 px-4 py-2 rounded-md text-white "
                                onClick={handleUpdateRole}
                            >
                                {updateRoleLoader ? "Loading..." : "Update Role"}
                            </button>
                        </div>

                        <hr className="py-2"/>
                        <div className="flex flex-col gap-4 py-4">
                            <div className="flex items-center gap-2">
                                <label className="text-slate-600 text-sm font-semibold uppercase">
                                    {" "}
                                    Lock Account
                                </label>
                                <input
                                    className="text-14 w-5 h-5"
                                    type="checkbox"
                                    name="lock"
                                    checked={!user?.accountNonLocked}
                                    onChange={(e) =>
                                        handleCheckboxChange(e, "/admin/update-lock-status")
                                    }
                                />
                            </div>
                            <div className="flex items-center gap-2">
                                <label className="text-slate-600 text-sm font-semibold uppercase">
                                    {" "}
                                    Account Verified
                                </label>
                                <input
                                    className="text-14 w-5 h-5"
                                    type="checkbox"
                                    name="verify"
                                    checked={user?.accountVerified}
                                    onChange={(e) =>
                                        handleCheckboxChange(e, "/admin/update-verification-status")
                                    }
                                />
                            </div>
                            <div className="flex items-center gap-2">
                                <label className="text-slate-600 text-sm font-semibold uppercase">
                                    {" "}
                                    Profiling Enabled
                                </label>
                                <input
                                    className="text-14 w-5 h-5"
                                    type="checkbox"
                                    name="profiling"
                                    checked={user?.profilingEnabled}
                                    onChange={(e) =>
                                        handleCheckboxChange(e, "/admin/update-profiling-status")
                                    }
                                />
                            </div>
                        </div>
                    </div>
                </>
            )}
        </div>
    )
}

export default UserDetails;