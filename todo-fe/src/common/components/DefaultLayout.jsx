import { Outlet } from "react-router";
import {Header} from "./Header.jsx";
import {useContext} from "react";
import {UserContext} from "../../providers/UserProvider.jsx";



const DefaultLayout = () => {

    const {user} = useContext(UserContext);

    const getTheme = (theme) => {
        switch (theme) {
            case "LIGHT":
                return "light";
            case "DARK":
                return "dark";
            default:
                return "light";
        }
    }


    return (
             <div data-theme={getTheme(user?.setting?.theme)}>
                <Header />
                <div className="content">
                    <Outlet />
                </div>
            </div>
    );
};

export default DefaultLayout;