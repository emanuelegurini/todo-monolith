import {createBrowserRouter} from "react-router";
import DefaultLayout from "./components/DefaultLayout.jsx";
import ErrorPage from "../pages/error-pages.jsx";
import HomePage from "../pages/HomePage.jsx";
import LoginPage from "../pages/LoginPage.jsx";
import { ProtectedRoute } from "./components/ProtectedRoute.jsx";
import ContactPage from "../pages/ContactPage.jsx";

export const router = createBrowserRouter([
    {
        path: "/",
        Component: DefaultLayout,
        errorElement: ErrorPage,
        children: [
            {
                path: "/",
                element: (
                    <ProtectedRoute>
                        <HomePage />
                    </ProtectedRoute>
                )
            },
            {
                path: "/contact",
                Component: ContactPage
            }

        ]
    },
    {
        path: "/auth/login",
        Component: LoginPage
    }
])