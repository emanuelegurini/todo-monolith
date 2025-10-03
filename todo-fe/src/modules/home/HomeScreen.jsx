import { useTranslation } from 'react-i18next';
import {useEffect, useState, useContext} from "react";
import {getUser} from "./models/user.js";
import {UserContext} from "../../providers/UserProvider.jsx";

const HomeScreen = () => {
    const { t } = useTranslation()

    const { user, setUser} = useContext(UserContext)

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const loadUser = async () => {
        try{
            const response = await getUser(14);
            setUser(response);
        } catch (error) {
            console.log(error);
            setError(error);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadUser()
    }, []);

    return (
        <div>
            <h1>Home page</h1>
            { user?.setting?.accountType === "FREE" && <h2>Hey, buy Pro account!</h2> }
            { user?.setting?.accountType === "PRO" && <h2>Hey, buy MAX account!</h2> }
        </div>
    )
}

export default HomeScreen;