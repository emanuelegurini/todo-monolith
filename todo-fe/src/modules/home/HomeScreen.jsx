import { useTranslation } from 'react-i18next';

const HomeScreen = () => {
    const { t } = useTranslation()

    return <h1>{t('pateta')}</h1>
}

export default HomeScreen;