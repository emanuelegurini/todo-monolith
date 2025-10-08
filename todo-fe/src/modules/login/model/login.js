import { fetchToken } from "../lib/api"

/*
* This functions return a clean version of the user
*/
export const getLoginToken = async (email, password) => {
	const response = await fetchToken(email, password)
	return { 
		token: response?.token,
		id: response?.id,
		name: response?.name, 
		email: response?.email
	}
}