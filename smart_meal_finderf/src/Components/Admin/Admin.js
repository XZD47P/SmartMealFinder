import {useMyContext} from "../../Store/ContextApi";
import AdminAreaSidebar from "./AdminAreaSidebar";
import {Route, Routes} from "react-router-dom";
import UserList from "./UserList";
import UserDetails from "./UserDetails";


const Admin = () => {
    //Oldalpanel nyitva levésének vizsgálata
    const {openSidebar} = useMyContext();

    return (
        <div className="flex">
            <AdminAreaSidebar/>
            <div
                className={`transition-all overflow-hidden flex-1 duration-150 w-full min-h-[calc(100vh-74px)] ${
                    openSidebar ? "lg:ml-52 ml-12" : "ml-12"
                }`}
            >
                <Routes>
                    <Route path="/users" element={<UserList/>}/>
                    <Route path="/users/:userId" element={<UserDetails/>}/>
                </Routes>
            </div>
        </div>
    )
}

export default Admin;