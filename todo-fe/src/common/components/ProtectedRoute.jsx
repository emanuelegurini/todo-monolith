import { Navigate, useLocation } from "react-router";
import { useAuth } from "../../core/auth";

/**
 * ProtectedRoute component
 * Protects routes from unauthenticated access
 * Redirects to login page and saves the original location for post-login redirect
 */
export const ProtectedRoute = ({ children }) => {
  const location = useLocation();
  const { isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/auth/login" state={{ from: location }} replace />;
  }

  return <>{children}</>;
};