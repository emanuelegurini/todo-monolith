/*
* This function return the data related to the user
*/
export const fetchUser = async(id) => {
    const response = await fetch(`${import.meta.env.VITE_BASE_URL}${import.meta.env.VITE_API_VERSION}/users/${id}`)
    if (!response.ok) {
        throw new Error(`HTTP Error: ${response.status}`)
    }
    return await response.json()
}