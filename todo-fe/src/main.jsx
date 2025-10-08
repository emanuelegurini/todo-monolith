import {StrictMode} from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { RouterProvider } from "react-router";
import './i18n';
import {router} from "./common/routes.jsx";
import {UserProvider} from "./providers/UserProvider.jsx";
import { AuthInitializer } from "./core/auth";

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <UserProvider>
          <AuthInitializer>
              <RouterProvider router={router} />
          </AuthInitializer>
      </UserProvider>
  </StrictMode>,
)
