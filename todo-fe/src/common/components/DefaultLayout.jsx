import { Outlet } from "react-router";

const DefaultLayout = () => {
    return (
            <div>
                <div className="content">
                    <Outlet />
                </div>
            </div>
    );
};

export default DefaultLayout;