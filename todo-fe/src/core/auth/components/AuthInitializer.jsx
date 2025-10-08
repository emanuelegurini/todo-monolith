import { useEffect, useState, useContext } from 'react';
import { UserContext } from '../../../providers/UserProvider';
import { fetchValidateToken, getToken, removeToken } from '../services/authService';

const AuthInitializer = ({ children }) => {
  const { setUser } = useContext(UserContext);
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    const initializeAuth = async () => {
      const token = getToken();

      if (token) {
        try {
          const user = await fetchValidateToken(token);
          setUser({
            name: user.name || '',
            email: user.email || '',
            isAuthenticated: true,
          });
        } catch (e) {
          removeToken();
          setUser({
            name: '',
            email: '',
            isAuthenticated: false,
          });
        }
      } else {
        setUser({
          name: '',
          email: '',
          isAuthenticated: false,
        });
      }

      setIsInitialized(true);
    };

    initializeAuth();
  }, [setUser]);

  if (!isInitialized) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900"></div>
      </div>
    );
  }

  return <>{children}</>;
};

export default AuthInitializer;
