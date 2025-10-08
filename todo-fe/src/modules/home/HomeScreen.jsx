import { useTranslation } from 'react-i18next';

const HomeScreen = () => {
    const { t } = useTranslation()

/*     const loadUser = async () => {
        try{
            const response = await getUser(14);
            setUser(response);
        } catch (error) {
            console.log(error);
            setError(error);
        } finally {
            setLoading(false);
        }
    } */

/*     useEffect(() => {
        loadUser()
    }, []); */

    return (
        <div>
            <h1>Home page</h1>
        </div>
    )
}

export default HomeScreen;