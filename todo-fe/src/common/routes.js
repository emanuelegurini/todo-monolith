import {createBrowserRouter} from "react-router";
import DefaultLayout from "./components/DefaultLayout.jsx";
import ErrorPage from "../pages/error-pages.jsx";
import HomePage from "../pages/HomePage.jsx";

export const router = createBrowserRouter([
    {
        path: "/",
        Component: DefaultLayout,
        errorElement: ErrorPage,
        children: [
            {
                path: "/",
                Component: HomePage
            }

        ]
    },
])