import { useContext } from 'react';
import { UserContext } from '../../../providers/UserProvider';
import { fetchToken, setToken, removeToken } from '../services/authService';

export const useAuth = () => {
  const { user, setUser } = useContext(UserContext);

  const login = async (email, password) => {
    try {
      const data = await fetchToken(email, password);
      setToken(data.token);
      setUser({
        name: data.name || '',
        email: data.email || '',
        isAuthenticated: true,
      });
      return { success: true };
    } catch (error) {
      return { success: false, error: error.message };
    }
  };

  const logout = () => {
    removeToken();
    setUser({
      name: '',
      email: '',
      isAuthenticated: false,
    });
  };

  return {
    user,
    login,
    logout,
    isAuthenticated: user.isAuthenticated,
  };
};
