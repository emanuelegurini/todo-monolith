import {useContext} from "react";
import {UserContext} from "../../providers/UserProvider.jsx";

export const Header = () => {

    const { user } = useContext(UserContext);

    return (
        <header className="p-4 bg-gray-300 dark:bg-pink-300">
            <div className="max-w-6xl px-4 mx-auto flex justify-between items-center">
                <h1 className="text-xl font-bold">{user?.name}</h1>
                { user?.setting?.accountType === "FREE" && <span className="">Free</span> }
                { user?.setting?.accountType === "PRO" && <span className="">Pro</span> }
                { user?.setting?.accountType === "MAX" && <span className="">Max</span> }
                <div className="flex items-center gap-4">
                </div>
            </div>
        </header>
    );
};