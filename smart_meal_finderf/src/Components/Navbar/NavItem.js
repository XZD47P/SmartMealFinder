import {Link, useLocation} from "react-router-dom";

const NavItem = ({to, children, onClick, className = ""}) => {
    const pathName = useLocation().pathname;

    const isActive = pathName === to || pathName.startsWith(to);

    return (
        <Link to={to} onClick={onClick} className={`py-2 cursor-pointer hover:text-white 
                    ${isActive ? "font-semibold" : ""} 
                    ${className}`}>
            {children}

        </Link>
    );
};

export default NavItem;