import {StrictMode} from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router";
import DefaultLayout from "./common/components/DefaultLayout.jsx";
import HomePage from "./pages/HomePage";
import ErrorPage from "./pages/error-pages.jsx";

const router = createBrowserRouter([
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

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
