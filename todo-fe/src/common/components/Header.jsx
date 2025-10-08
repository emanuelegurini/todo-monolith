import { useAuth } from "../../core/auth";
import { useNavigate } from "react-router";

export const Header = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/auth/login");
    };

    return (
        <header className="p-4 bg-gray-300 dark:bg-pink-300">
            <div className="max-w-6xl px-4 mx-auto flex justify-between items-center">
                <h1 className="text-xl font-bold">{user?.name}</h1>
                <div className="flex items-center gap-4">
                    {user?.isAuthenticated && (
                        <button
                            onClick={handleLogout}
                            className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600 transition-colors"
                        >
                            Logout
                        </button>
                    )}
                </div>
            </div>
        </header>
    );
};