import {MdDateRange, MdOutlineEmail} from "react-icons/md";
import {Link} from "react-router-dom";
import {useEffect, useState} from "react";
import api from "../../Backend/api";
import toast from "react-hot-toast";
import moment from "moment";
import Errors from "../Errors";
import {DataGrid} from "@mui/x-data-grid";
import {ClockLoader} from "react-spinners";

export const userListColumns = [
    {
        field: "username",
        headerName: "Username",
        minWidth: 200,
        headerAlign: "center",
        disableColumnMenu: true,
        align: "center",
        editable: false,
        headerClassName: "text-black font-semibold border",
        cellClassName: "text-slate-700 font-normal  border",
        renderHeader: (params) => <span className="text-center">Username</span>,
    },
    {
        field: "email",
        headerName: "Email",
        aligh: "center",
        width: 260,
        editable: false,
        headerAlign: "center",
        headerClassName: "text-black font-semibold text-center border ",
        cellClassName: "text-slate-700 font-normal  border  text-center ",
        align: "center",
        disableColumnMenu: true,
        renderHeader: (params) => <span>Email</span>,
        renderCell: (params) => {
            return (
                <div className=" flex  items-center justify-center  gap-1 ">
          <span>
            <MdOutlineEmail className="text-slate-700 text-lg"/>
          </span>
                    <span>{params?.row?.email}</span>
                </div>
            );
        },
    },
    {
        field: "status",
        headerName: "Status",
        headerAlign: "center",
        align: "center",
        width: 200,
        editable: false,
        disableColumnMenu: true,
        headerClassName: "text-black font-semibold border ",
        cellClassName: "text-slate-700 font-normal  border  ",
        renderHeader: (params) => <span className="ps-10">Status</span>,
    },
    {
        field: "verification",
        headerName: "Verification",
        headerAlign: "center",
        align: "center",
        width: 200,
        editable: false,
        disableColumnMenu: true,
        headerClassName: "text-black font-semibold border ",
        cellClassName: "text-slate-700 font-normal  border  ",
        renderHeader: (params) => <span className="ps-10">Verification</span>,
    },
    {
        field: "signUpMethod",
        headerName: "Signup Method",
        headerAlign: "center",
        align: "center",
        width: 200,
        editable: false,
        disableColumnMenu: true,
        headerClassName: "text-black font-semibold border ",
        cellClassName: "text-slate-700 font-normal  border  ",
        renderHeader: (params) => <span className="ps-10">Signup Method</span>,
    },
    {
        field: "created",
        headerName: "Created At",
        headerAlign: "center",
        width: 220,
        editable: false,
        headerClassName: "text-black font-semibold border",
        cellClassName: "text-slate-700 font-normal  border  ",
        align: "center",
        disableColumnMenu: true,
        renderHeader: (params) => <span>Created At</span>,
        renderCell: (params) => {
            return (
                <div className=" flex justify-center  items-center  gap-1 ">
          <span>
            <MdDateRange className="text-slate-700 text-lg"/>
          </span>
                    <span>{params?.row?.created}</span>
                </div>
            );
        },
    },
    {
        field: "action",
        headerName: "Action",
        headerAlign: "center",
        editable: false,
        headerClassName: "text-black font-semibold text-cente",
        cellClassName: "text-slate-700 font-normal",
        sortable: false,
        width: 200,
        renderHeader: (params) => <span>Action</span>,
        renderCell: (params) => {
            return (
                <Link
                    to={`/admin/users/${params.id}`}
                    className="h-full flex  items-center justify-center   "
                >
                    <button className="bg-btnColor text-white px-4 flex justify-center items-center  h-9 rounded-md ">
                        View
                    </button>
                </Link>
            );
        },
    },
];

const UserList = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(false);

    //Betöltés után a felhasználók összegyűjtése
    useEffect(() => {
        setLoading(true);
        const fetchUsers = async () => {
            try {
                const response = await api.get("/admin/getusers");
                const usersData = Array.isArray(response.data) ? response.data : [];
                setUsers(usersData);
            } catch (error) {
                setError(error?.response?.data?.message);
                toast.error("Error fetching users", error);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    //Felhasználók sorokhoz rendelése
    const rows = users.map((item) => {
        const formattedDate = moment(item.createdAt).format("YYYY-MM-DD, HH:mm:ss");

        return {
            id: item.userId,
            username: item.userName,
            email: item.email,
            created: formattedDate,
            status: item.accountNonLocked ? "Active" : "Locked",
            verification: item.accountVerified,
            signUpMethod: item.signUpMethod,
        };
    });

    if (error) {
        return <Errors message={error}/>
    }

    return (
        <div className="p-4">
            <div className="py-4">
                <h1 className="text-center text-2xl font-bold text-slate-800 uppercase">
                    All Users
                </h1>
            </div>
            <div className="overflow-x-auto w-full mx-auto">
                {loading ? (
                    <>
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
                        <DataGrid
                            className="w-fit mx-auto"
                            rows={rows}
                            columns={userListColumns}
                            initialState={{
                                pagination: {
                                    paginationModel: {
                                        pageSize: 6,
                                    },
                                },
                            }}
                            disableRowSelectionOnClick
                            pageSizeOptions={[6]}
                            disableColumnResize
                        />
                    </>
                )}
            </div>
        </div>
    );

}

export default UserList;