import {useState, createContext} from "react";

export const UserContext = createContext({
    user: null,
    setUser: () => {}
})

export const UserProvider = ({children}) => {
    const [user, setUser] = useState({
        name: '',
        email: '',
        isAuthenticated: false
    });

    return (
        <UserContext.Provider value={{user, setUser}}>
            {children}
        </UserContext.Provider>
    );
}